package com.jewelrymanagement.service;

import com.jewelrymanagement.dto.OrderDetailsDTO;
import com.jewelrymanagement.entity.Order;
import com.jewelrymanagement.entity.OrderDetails;
import com.jewelrymanagement.entity.Product;
import com.jewelrymanagement.entity.ProductDetails;
import com.jewelrymanagement.model.StatusResponse;
import com.jewelrymanagement.repository.OrderDetailsRepository;
import com.jewelrymanagement.repository.OrderRepository;
import com.jewelrymanagement.repository.ProductDetailsRepository;
import com.jewelrymanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderDetailsService {
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    public OrderDetailsDTO convertToDTO(OrderDetails orderDetails){
        OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO();
        orderDetailsDTO.order_details_id = orderDetails.getOrder_details_id();
        orderDetailsDTO.order_id = orderDetails.getOrder_id().getOrder_id();
        orderDetailsDTO.product_id = orderDetails.getProduct().getProduct_id();
        orderDetailsDTO.product_details_id = orderDetails.getProduct_details_id();
        orderDetailsDTO.quantity = orderDetails.getQuantity();
        orderDetailsDTO.unit_price = orderDetails.getUnit_price();
        orderDetailsDTO.discount = orderDetails.getDiscount();
        return orderDetailsDTO;
    }

    public OrderDetails convertToEntity(OrderDetailsDTO orderDetailsDTO){
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrder_details_id(orderDetailsDTO.order_details_id);
        Order orderID = orderRepository.findById(orderDetailsDTO.order_id).orElseThrow();
        orderDetails.setOrder_id(orderID);
        Product productID = productRepository.findById(orderDetailsDTO.product_id).orElseThrow();
        orderDetails.setProduct(productID);
        orderDetails.setProduct_details_id(orderDetailsDTO.product_details_id);
        orderDetails.setQuantity(orderDetailsDTO.quantity);
        orderDetails.setUnit_price(orderDetailsDTO.unit_price);
        orderDetails.setDiscount(orderDetailsDTO.discount);
        return orderDetails;
    }

    public StatusResponse<List<OrderDetailsDTO>> getAllOrderDetails(){
        try{
            List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
            List<OrderDetailsDTO> orderDetailsDTOList = orderDetailsList.stream().map(this::convertToDTO).collect(Collectors.toList());
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success","OrderDetails list retrieved successfully", orderDetailsDTOList);
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error","An unexpected error occurred",null);
        }
    }

    public StatusResponse<OrderDetailsDTO> getOrderDetailsById(Integer id) {
        try {
            Optional<OrderDetails> orderDetails = orderDetailsRepository.findById(id);
            if (orderDetails.isPresent()) {
                OrderDetails orderDetailsSave = orderDetails.get();
                OrderDetailsDTO orderDetailsDTO = convertToDTO(orderDetailsSave);
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "OrderDetails successfully", orderDetailsDTO);
            } else {
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "OrderDetails not found", null);
            }
        } catch (Exception e) {
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "An unexpected error occurred", null);
        }
    }

    public StatusResponse<OrderDetailsDTO> addOrderDetails(OrderDetailsDTO orderDetailsDTO) {
        try {
            Optional<ProductDetails> productDetailsOptional = productDetailsRepository.findById(orderDetailsDTO.product_details_id);

            Order order = orderRepository.findById(orderDetailsDTO.order_id).orElseThrow();

            if (productDetailsOptional.isEmpty()) {
                return new StatusResponse<>(
                        UUID.randomUUID().toString(),
                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                        "Error",
                        "Product details not found",
                        null
                );
            }

            ProductDetails productDetails = productDetailsOptional.get();


            if (productDetails.getQuantity() < orderDetailsDTO.quantity)
            {
                return new StatusResponse<>(
                        UUID.randomUUID().toString(),
                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                        "Error",
                        "Insufficient product quantity",
                        null
                );
            }

            OrderDetails orderDetailsSave = convertToEntity(orderDetailsDTO);
            orderDetailsSave.setUnit_price(productDetails.getPrice().multiply(BigDecimal.valueOf(orderDetailsDTO.quantity)));
            orderDetailsSave.setProduct_details_id(orderDetailsDTO.product_details_id);
            orderDetailsSave = orderDetailsRepository.save(orderDetailsSave);
            OrderDetailsDTO orderDetailsDTOSave = convertToDTO(orderDetailsSave);
            return new StatusResponse<>(
                    UUID.randomUUID().toString(),
                    LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                    "Success",
                    "Order created successfully",
                    orderDetailsDTOSave
            );
        } catch (Exception e) {
            return new StatusResponse<>(
                    UUID.randomUUID().toString(),
                    LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                    "Error",
                    "An unexpected error occurred",
                    null
            );
        }
    }






    public StatusResponse<OrderDetailsDTO> updateOrderDetails(Integer id, OrderDetailsDTO orderDetailsDTO) {
        try {
            // Kiểm tra sự tồn tại của OrderDetails
            OrderDetails existingOrderDetails = orderDetailsRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order details not found"));

            // Lấy thông tin sản phẩm
            ProductDetails productDetails = productDetailsRepository.findById(orderDetailsDTO.product_id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Kiểm tra số lượng sản phẩm
            if (productDetails.getQuantity() < orderDetailsDTO.quantity) {
                return new StatusResponse<>(
                        UUID.randomUUID().toString(),
                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                        "Error",
                        "Insufficient product quantity",
                        null
                );
            }

            // Cập nhật OrderDetails
            OrderDetails orderDetailsSave = convertToEntity(orderDetailsDTO);
            orderDetailsSave.setOrder_details_id(id);
            OrderDetails updatedOrderDetails = orderDetailsRepository.save(existingOrderDetails);

            // Trả về phản hồi thành công
            OrderDetailsDTO updatedOrderDetailsDTO = convertToDTO(updatedOrderDetails);
            return new StatusResponse<>(
                    UUID.randomUUID().toString(),
                    LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                    "Success",
                    "Order details updated successfully",
                    updatedOrderDetailsDTO
            );
        } catch (Exception e) {
            // Trả về phản hồi lỗi
            return new StatusResponse<>(
                    UUID.randomUUID().toString(),
                    LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                    "Error",
                    "An unexpected error occurred",
                    null
            );
        }
    }

    public StatusResponse<OrderDetailsDTO> deleteOrderDetails(Integer id) {
        try {
            OrderDetails orderDetails = orderDetailsRepository.findById(id).orElseThrow(() -> new RuntimeException("Order details not found"));
            orderDetailsRepository.delete(orderDetails);
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Order details delete successfully", null);
        } catch (Exception e) {
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Delete error", null);
        }
    }


public StatusResponse<List<OrderDetailsDTO>> getAllOrderDetailsById(Integer id) {
        try {
            List<OrderDetails> orderDetailsList = orderDetailsRepository.findOrderDetailsByOrderId(id);
            List<OrderDetailsDTO> orderDetailsDTOList = orderDetailsList.stream().map(this::convertToDTO).collect(Collectors.toList());
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "OrderDetails list retrieved successfully", orderDetailsDTOList);
        }catch (Exception e) {
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "An unexpected error occurred", null);
        }
}

public StatusResponse<?> deleteAllOrderDetailsOfOrderId(Integer id) {
        try {
            orderDetailsRepository.deleteOrderDetailsByOrderId(id);
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "OrderDetails delete successfully", null);
        }catch (Exception e) {
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Delete error", null);
        }
}

}
