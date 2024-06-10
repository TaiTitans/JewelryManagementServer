package com.jewelrymanagement.service;

import com.jewelrymanagement.dto.OrderDTO;
import com.jewelrymanagement.dto.OrderDetailsDTO;
import com.jewelrymanagement.dto.OrderInvoiceDTO;
import com.jewelrymanagement.entity.Customer;
import com.jewelrymanagement.entity.OrderDetails;
import com.jewelrymanagement.entity.ProductDetails;
import com.jewelrymanagement.model.MonthlyReport;
import com.jewelrymanagement.entity.Order;
import com.jewelrymanagement.exceptions.OrderStatus;
import com.jewelrymanagement.repository.*;
import com.jewelrymanagement.model.StatusResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private GetUsernameRepository GetUsernameRepository;
    @Autowired
    private GetUsernameRepository getUsernameRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    public OrderDTO convertToDTO(Order order){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.order_id = order.getOrder_id();
        orderDTO.status = order.getStatus();
        orderDTO.notes = order.getNotes();
        orderDTO.customer_id = order.getCustomer().getCustomer_id();
        orderDTO.payment_method = order.getPayment_method();
        orderDTO.shipping_fee = order.getShipping_fee();
        orderDTO.total_amount = order.getTotal_amount();
        orderDTO.created_at = order.getCreated_at();
        orderDTO.updated_at = order.getUpdated_at();
        return orderDTO;
    }


    public Order convertToEntity(OrderDTO orderDTO){
        Order order = new Order();
        order.setOrder_id(orderDTO.order_id);
        order.setStatus(orderDTO.status);
        order.setNotes(orderDTO.notes);
        order.setShipping_fee(orderDTO.shipping_fee);
        order.setTotal_amount(orderDTO.total_amount);
        order.setPayment_method(orderDTO.payment_method);
        order.setCreated_at(orderDTO.created_at);
        Customer customer = customerRepository.findById(orderDTO.customer_id).orElseThrow();
        order.setCustomer(customer);
        return order;
    }
    public StatusResponse<List<OrderDTO>> getAllOrder(){
        try{
            List<Order> orderList = orderRepository.findAll();
            List<OrderDTO> orderDTOList = orderList.stream().map(this::convertToDTO).collect(Collectors.toList());
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success","Order list retrieved successfully", orderDTOList);
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error","An unexpected error occurred",null);
        }
    }

    public StatusResponse<OrderDTO> getOrderById(Integer id){
        try {
            Optional<Order> orderOptional = orderRepository.findById(id);
            if(orderOptional.isPresent()){
                Order order = orderOptional.get();
                OrderDTO orderDTO = convertToDTO(order);
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Order retrieved successfully", orderDTO);
            }else{
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Order not found", null);
            }
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "An unexpected error occurred", null);
        }
    }

    public StatusResponse<OrderDTO> createOrder(OrderDTO orderDTO, HttpServletRequest request) {
        try {
            String username = getUsernameFromCookie(request);
            if (username == null) {
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "User not authenticated", null);
            }

            Order order = convertToEntity(orderDTO);
            order.setCreated_by(username);
            order.setCreated_at(new Date());
            order.setUpdated_at(new Date());
            order.setShipping_fee(BigDecimal.valueOf(30000));
            order = orderRepository.save(order);
            OrderDTO orderDTOSaved = convertToDTO(order);

            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Order created successfully", orderDTOSaved);
        } catch (Exception e) {
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Create error: " + e.getMessage(), null);
        }
    }

    public StatusResponse<OrderDTO> updateOrder(Integer id, OrderDTO orderDTO, HttpServletRequest request){
        try{
            String username = getUsernameFromCookie(request);
            if(orderRepository.existsById(id)){
                Order oldOrder = orderRepository.findById(id).orElse(null);
                Order order = convertToEntity(orderDTO);
                order.setOrder_id(id);
                order.setCreated_at(oldOrder.getCreated_at());
                order.setCreated_by(username);
                order.setUpdated_at(new Date());
                order = orderRepository.save(order);
                OrderDTO orderDTOSaved = convertToDTO(order);
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Order update successfully", orderDTOSaved);
            }else{
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "ID Product exits", null);
            }
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Update error", null);
        }
    }


    public StatusResponse<OrderDTO> deleteOrder(Integer id){
        try{
            orderRepository.deleteById(id);
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Order delete successfully", null);
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Delete error", null);
        }
    }

    private String getUsernameFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


    public BigDecimal getTotalAmountForToday() {
        LocalDate currentDate = LocalDate.now();
        Date startDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(currentDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());

        List<Order> orders = orderRepository.findOrdersByDateRange(startDate, endDate);

        BigDecimal totalAmount = orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.COMPLETE) // Chỉ tính toán với trạng thái COMPLETE
                .map(Order::getTotal_amount) // Trực tiếp lấy giá trị BigDecimal bằng method reference
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalAmount;
    }



    public MonthlyReport getMonthlyReport(int month, int year) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startLocalDate = yearMonth.atDay(1);
        LocalDate endLocalDate = yearMonth.atEndOfMonth();

        Date startDate = Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Order> orders = orderRepository.findOrdersByDateRange(startDate, endDate);

        int totalOrders = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.COMPLETE) { // Chỉ tính toán với trạng thái COMPLETE
                totalOrders++;
                totalAmount = totalAmount.add(order.getTotal_amount()); // Trực tiếp sử dụng giá trị BigDecimal
            }
        }


        return new MonthlyReport(totalOrders, totalAmount);
    }

    public BigDecimal getTotalAmount() {
        List<Order> orders = orderRepository.findAll();

        BigDecimal totalAmount = orders.stream().filter(order -> order.getStatus() == OrderStatus.COMPLETE)
                .map(Order::getTotal_amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalAmount;
    }


    public OrderDTO updateOrderStatus(Integer orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow();

        order.setStatus(newStatus);
        orderRepository.save(order);
        OrderDTO orderDTO = convertToDTO(order);
        return orderDTO;
    }

    //Xuat hoa don

    public OrderInvoiceDTO getOrderInvoice(Integer orderId) {
        // Lấy Order từ database
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        // Lấy danh sách OrderDetails
        List<OrderDetails> orderDetails = orderDetailsRepository.findOrderDetailsByOrderId(orderId);

        // Tính toán total_amount
        BigDecimal totalAmount = calculateTotalAmount(orderDetails, order.getShipping_fee());

        // Cập nhật lại total_amount trong bảng Orders
        order.setTotal_amount(totalAmount);
        orderRepository.save(order);

        // Cập nhật trạng thái Order thành "COMPLETE"
        order.setStatus(OrderStatus.COMPLETE);
        orderRepository.save(order);

        // Cập nhật total_orders và total_spent trong bảng Customers
        updateCustomerTotals(order, orderDetails);

        // Tạo và trả về OrderInvoiceDTO
        OrderInvoiceDTO orderInvoiceDTO = new OrderInvoiceDTO();
        orderInvoiceDTO.setOrder_id(order.getOrder_id());
        orderInvoiceDTO.setCustomer_name(order.getCustomer().getCustomer_name());
        orderInvoiceDTO.setPayment_method(order.getPayment_method().name());
        orderInvoiceDTO.setOrderDetails(convertToOrderDetailsDTO(orderDetails));
        orderInvoiceDTO.setShipping_fee(order.getShipping_fee());
        orderInvoiceDTO.setTotal_amount(totalAmount);

        return orderInvoiceDTO;
    }

    private BigDecimal calculateTotalAmount(List<OrderDetails> orderDetails, BigDecimal shippingFee) {
        return orderDetails.stream()
                .map(OrderDetails::getUnit_price)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .subtract(shippingFee);
    }

    public void updateCustomerTotals(Order order, List<OrderDetails> orderDetails) {
        Customer customer = order.getCustomer();

        // Cập nhật total_orders
        customer.setTotal_orders(customer.getTotal_orders().add(order.getTotal_amount()));

        // Cập nhật total_points
        BigDecimal totalPoints = orderDetails.stream()
                .map(od -> BigDecimal.valueOf(od.getProductdetails().getPoints()).multiply(BigDecimal.valueOf(od.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        customer.setTotal_points(customer.getTotal_points().add(totalPoints));

        customerRepository.save(customer);
    }

    private void updateProductDetailsQuantity(List<OrderDetails> orderDetails) {
        for (OrderDetails orderDetail : orderDetails) {
            ProductDetails productDetails = productDetailsRepository.findById(orderDetail.getProductdetails().getProduct_details_id())
                    .orElseThrow(() -> new EntityNotFoundException("Product details not found with id: " + orderDetail.getProductdetails().getProduct_details_id()));

            int newQuantity = productDetails.getQuantity() - orderDetail.getQuantity();
            productDetails.setQuantity(newQuantity);
            productDetailsRepository.save(productDetails);
        }
    }

    private List<OrderDetailsDTO> convertToOrderDetailsDTO(List<OrderDetails> orderDetails) {
        return orderDetails.stream()
                .map(this::convertToOrderDetailsDTO)
                .collect(Collectors.toList());
    }

    private OrderDetailsDTO convertToOrderDetailsDTO(OrderDetails orderDetails) {
        OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO();
        orderDetailsDTO.order_details_id = orderDetails.getOrder_details_id();
        orderDetailsDTO.order_id = orderDetails.getOrder_id().getOrder_id();
        orderDetailsDTO.product_id = orderDetails.getProduct().getProduct_id();
        orderDetailsDTO.product_details_id = orderDetails.getProductdetails().getProduct_details_id();
        orderDetailsDTO.quantity = orderDetails.getQuantity();
        orderDetailsDTO.unit_price = orderDetails.getUnit_price();
        orderDetailsDTO.discount = orderDetails.getDiscount();
        return orderDetailsDTO;
    }
}
