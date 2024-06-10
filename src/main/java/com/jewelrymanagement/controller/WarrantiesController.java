package com.jewelrymanagement.controller;


import com.jewelrymanagement.dto.WarrantiesDTO;
import com.jewelrymanagement.entity.Warranties;
import com.jewelrymanagement.exceptions.WarrantyStatus;
import com.jewelrymanagement.model.StatusResponse;
import com.jewelrymanagement.service.WarrantiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WarrantiesController {
    @Autowired
    private WarrantiesService warrantiesService;

    @GetMapping("/common/warranty")
    public ResponseEntity<StatusResponse<List<WarrantiesDTO>>> getAllWarranties() {
        StatusResponse<List<WarrantiesDTO>> response = warrantiesService.getAllWarranty();
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @GetMapping("/common/warranty/{id}")
    public ResponseEntity<StatusResponse<WarrantiesDTO>> getWarrantiesById(@PathVariable int id) {
        StatusResponse<WarrantiesDTO> response = warrantiesService.getWarrantyById(id);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @PostMapping("/common/warranty")
    public ResponseEntity<StatusResponse<WarrantiesDTO>> addWarrantie(@RequestBody WarrantiesDTO warrantiesDTO) {
        StatusResponse<WarrantiesDTO> response = warrantiesService.addWarranties(warrantiesDTO);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @PutMapping("/manager/warranty/{id}")
    public ResponseEntity<StatusResponse<WarrantiesDTO>> updateWarranties(@PathVariable int id, @RequestBody WarrantiesDTO warrantiesDTO) {
        StatusResponse<WarrantiesDTO> response = warrantiesService.updateWarranties(id, warrantiesDTO);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @DeleteMapping("/manager/warranty/{id}")
    public ResponseEntity<StatusResponse<WarrantiesDTO>> deleteWarranties(@PathVariable int id) {
        StatusResponse<WarrantiesDTO> response = warrantiesService.deleteWarrantyById(id);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }


    @GetMapping("/common/customer/{id}/warranty")
    public ResponseEntity<StatusResponse<List<WarrantiesDTO>>> getCustomersWarranty(@PathVariable int id) {
        StatusResponse<List<WarrantiesDTO>> response = warrantiesService.getAllWarrantiesByCustomerId(id);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @DeleteMapping("/manager/customer/{id}/warranty")
    public ResponseEntity<StatusResponse<WarrantiesDTO>> deleteCustomersWarranty(@PathVariable int id) {
        StatusResponse<WarrantiesDTO> response = warrantiesService.deleteAllWarrantiesOfCustomerId(id);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @PatchMapping("/common/warranty/{id}/status")
    public WarrantiesDTO updateStatus(@PathVariable int id, @RequestBody WarrantyStatus warrantyStatus) {
        WarrantiesDTO warrantiesUpdate = warrantiesService.updateWarrantyStatus(id, warrantyStatus);
        return warrantiesUpdate;
    }


}
