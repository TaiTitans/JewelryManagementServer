package com.jewelrymanagement.controller;


import com.jewelrymanagement.dto.CustomerDTO;
import com.jewelrymanagement.service.CustomerService;
import com.jewelrymanagement.model.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/common/customer")
    public ResponseEntity<StatusResponse<List<CustomerDTO>>> getAllCustomer(){
        StatusResponse<List<CustomerDTO>> response =  customerService.getAllCustomer();
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/common/customer/{id}")
    public ResponseEntity<StatusResponse<CustomerDTO>> getCustomerById(@PathVariable int id){
        StatusResponse<CustomerDTO> response = customerService.getCustomerById(id);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else if("Customer not found".equals(response.getMessage())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/common/customer")
    public ResponseEntity<StatusResponse<CustomerDTO>> createCustomer(@RequestBody CustomerDTO customerDTO){
        StatusResponse<CustomerDTO> response = customerService.createCustomer(customerDTO);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/manager/customer/{id}")
    public ResponseEntity<StatusResponse<CustomerDTO>> updateCustomer(@PathVariable int id, @RequestBody CustomerDTO customerDTO){
        StatusResponse<CustomerDTO> response = customerService.updateCustomer(id, customerDTO);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/manager/customer/{id}")
    public ResponseEntity<StatusResponse<CustomerDTO>> deleteCustomer(@PathVariable int id){
        StatusResponse<CustomerDTO> response = customerService.deleteCustomer(id);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.badRequest().body(response);
        }
    }
}
