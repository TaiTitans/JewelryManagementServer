package com.jewelrymanagement.service;

import com.jewelrymanagement.dto.OrderDTO;
import com.jewelrymanagement.entity.Customer;
import com.jewelrymanagement.model.MonthlyReport;
import com.jewelrymanagement.entity.Order;
import com.jewelrymanagement.exceptions.OrderStatus;
import com.jewelrymanagement.repository.CustomerRepository;
import com.jewelrymanagement.repository.GetUsernameRepository;
import com.jewelrymanagement.repository.OrderRepository;
import com.jewelrymanagement.model.StatusResponse;
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
                .map(order -> BigDecimal.valueOf(order.getTotal_amount()))
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
                totalAmount = totalAmount.add(BigDecimal.valueOf(order.getTotal_amount()));
            }
        }

        return new MonthlyReport(totalOrders, totalAmount);
    }

    public BigDecimal getTotalAmount() {
        List<Order> orders = orderRepository.findAll();

        BigDecimal totalAmount = orders.stream().filter(order -> order.getStatus() == OrderStatus.COMPLETE)
                .map(order -> BigDecimal.valueOf(order.getTotal_amount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalAmount;
    }


    public Order updateOrderStatus(Integer orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow();

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
}
