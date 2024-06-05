package com.jewelrymanagement.controller;

import com.jewelrymanagement.dto.SupplierDTO;
import com.jewelrymanagement.service.SupplierService;
import com.jewelrymanagement.util.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @GetMapping("/common/supplier")
    public ResponseEntity<StatusResponse<List<SupplierDTO>>> getAllSuppliers() {
        StatusResponse<List<SupplierDTO>> response = supplierService.getAllSuppliers();
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/common/supplier/{id}")
    public ResponseEntity<StatusResponse<SupplierDTO>> getSupplierById(@PathVariable int id) {
        StatusResponse<SupplierDTO> response = supplierService.getSupplierById(id);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else if("Supplier not found".equals(response.getMessage())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/manager/supplier")
    public ResponseEntity<StatusResponse<SupplierDTO>> createSupplier(@RequestBody SupplierDTO supplierDTO) {
        StatusResponse<SupplierDTO> response = supplierService.createSupplier(supplierDTO);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/manager/supplier/{id}")
    public ResponseEntity<StatusResponse> updateSupplier(@PathVariable int id, @RequestBody SupplierDTO supplierDTO) {
        StatusResponse response = supplierService.updateSupplier(id, supplierDTO);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else if("Supplier not found".equals(response.getMessage())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/manager/supplier/{id}")
    public ResponseEntity<StatusResponse> deleteSupplier(@PathVariable int id) {
        StatusResponse response = supplierService.deleteSupplier(id);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else if("Supplier not found".equals(response.getMessage())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}
