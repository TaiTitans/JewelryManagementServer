package com.jewelrymanagement.controller;

import com.jewelrymanagement.dto.OrderDTO;
import com.jewelrymanagement.dto.OrderInvoiceDTO;
import com.jewelrymanagement.model.MonthlyReport;
import com.jewelrymanagement.entity.Order;
import com.jewelrymanagement.exceptions.OrderStatus;
import com.jewelrymanagement.service.OrderService;
import com.jewelrymanagement.model.StatusResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/common/orders")
    public ResponseEntity<StatusResponse<List<OrderDTO>>> getAllOrder(){
        StatusResponse<List<OrderDTO>> response = orderService.getAllOrder();
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/common/order/{id}")
    public ResponseEntity<StatusResponse<OrderDTO>> getOrderById(@PathVariable int id){
        StatusResponse<OrderDTO> response = orderService.getOrderById(id);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/common/order")
    public ResponseEntity<StatusResponse<OrderDTO>> createOrder(@RequestBody OrderDTO orderDTO, HttpServletRequest request){
        StatusResponse<OrderDTO> response = orderService.createOrder(orderDTO, request);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/manager/order/{id}")
    public ResponseEntity<StatusResponse<OrderDTO>> updateOrder(@PathVariable int id, @RequestBody OrderDTO orderDTO, HttpServletRequest request){
        StatusResponse<OrderDTO> response = orderService.updateOrder(id, orderDTO, request);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/manager/order/{id}")
    public ResponseEntity<StatusResponse> deleteOrder(@PathVariable int id){
        StatusResponse response = orderService.deleteOrder(id);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/manager/total-amount-today")
    public BigDecimal getTotalAmountToday() {
        return orderService.getTotalAmountForToday();
    }

    @GetMapping("/manager/monthly-report")
    public MonthlyReport getMonthlyReport(
            @RequestParam int month,
            @RequestParam int year) {
        MonthlyReport report = orderService.getMonthlyReport(month, year);
        return report;
    }
    @PatchMapping("/common/order/{orderId}/status")
    public OrderDTO updateOrderStatus(
            @PathVariable Integer orderId,
            @RequestBody OrderStatus status) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, status);
        return updatedOrder;
    }

    @GetMapping("/common/order/totalamount")
    public BigDecimal getTotalAmount() {
        return orderService.getTotalAmount();
    }

    //Xuat hoa don
    @GetMapping("/common/order/invoice/{orderId}")
    public ResponseEntity<OrderInvoiceDTO> getOrderInvoice(@PathVariable Integer orderId) {
        OrderInvoiceDTO orderInvoiceDTO = orderService.getOrderInvoice(orderId);
        return ResponseEntity.ok(orderInvoiceDTO);
    }
}
