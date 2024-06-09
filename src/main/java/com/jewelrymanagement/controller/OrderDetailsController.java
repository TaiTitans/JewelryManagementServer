package com.jewelrymanagement.controller;

import com.jewelrymanagement.dto.OrderDetailsDTO;
import com.jewelrymanagement.model.StatusResponse;
import com.jewelrymanagement.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderDetailsController {
    @Autowired
    private OrderDetailsService orderDetailsService;

    @GetMapping("/common/order/details")
    public ResponseEntity<StatusResponse<List<OrderDetailsDTO>>> getAllOrderDetails(){
        StatusResponse<List<OrderDetailsDTO>> response = orderDetailsService.getAllOrderDetails();
        if("Success".equals(response.getStatus())){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @GetMapping("/common/orderdetails/{id}")
    public ResponseEntity<StatusResponse<OrderDetailsDTO>> getOrderDetailsById(@PathVariable Integer id){
        StatusResponse<OrderDetailsDTO> response = orderDetailsService.getOrderDetailsById(id);
        if("Success".equals(response.getStatus())){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @PostMapping("/common/orderdetails")
    public ResponseEntity<StatusResponse<OrderDetailsDTO>> addOrderDetails(@RequestBody OrderDetailsDTO orderDetailsDTO){
        StatusResponse<OrderDetailsDTO> response = orderDetailsService.addOrderDetails(orderDetailsDTO);
        if("Success".equals(response.getStatus())){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @PutMapping("/manager/orderdetails/{id}")
    public ResponseEntity<StatusResponse<OrderDetailsDTO>> updateOrderDetails(@PathVariable Integer id, @RequestBody OrderDetailsDTO orderDetailsDTO){
        StatusResponse<OrderDetailsDTO> response = orderDetailsService.updateOrderDetails(id, orderDetailsDTO);
        if("Success".equals(response.getStatus())){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @DeleteMapping("/common/orderdetails/{id}")
    public ResponseEntity<StatusResponse> deleteOrderDetails(@PathVariable Integer id){
        StatusResponse response = orderDetailsService.deleteOrderDetails(id);
        if("Success".equals(response.getStatus())){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @GetMapping("/common/order/{id}/details")
    public ResponseEntity<StatusResponse<List<OrderDetailsDTO>>> getOrderDetailsByOrderId(@PathVariable Integer id){
        StatusResponse<List<OrderDetailsDTO>> response = orderDetailsService.getAllOrderDetailsById(id);
        if("Success".equals(response.getStatus())){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @DeleteMapping("/manager/order/{id}/details")
    public ResponseEntity<StatusResponse> deleteAllOrderDetailsByOrderId(@PathVariable Integer id){
        StatusResponse response = orderDetailsService.deleteAllOrderDetailsOfOrderId(id);
        if("Success".equals(response.getStatus())){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

}
