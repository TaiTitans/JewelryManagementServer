package com.jewelrymanagement.controller;

import com.jewelrymanagement.dto.FoundsDTO;
import com.jewelrymanagement.exceptions.Found.TransactionType;
import com.jewelrymanagement.repository.FoundsRepository;
import com.jewelrymanagement.service.FoundsService;
import com.jewelrymanagement.util.StatusResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/found")
public class FoundsController {
    @Autowired
    private FoundsService foundsService;
    @Autowired
    private FoundsRepository foundsRepository;

    @GetMapping
    public ResponseEntity<StatusResponse<List<FoundsDTO>>> getAllFound() {
        StatusResponse<List<FoundsDTO>> response = foundsService.getAllFounds();
        if ("Success".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<StatusResponse<FoundsDTO>> getFoundById(@PathVariable int id) {
        StatusResponse<FoundsDTO> response = foundsService.getFoundById(id);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else if ("Found not found".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping
    public ResponseEntity<StatusResponse<FoundsDTO>> createFound(@Valid @RequestBody FoundsDTO foundsDTO) {
        StatusResponse<FoundsDTO> response = foundsService.createFound(foundsDTO);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusResponse<FoundsDTO>> updateFound(@PathVariable int id, @RequestBody FoundsDTO foundsDTO) {
        StatusResponse<FoundsDTO> response = foundsService.updateFound(id, foundsDTO);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else if("Found not found".equals(response.getMessage())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
public ResponseEntity<StatusResponse<FoundsDTO>> deleteFound (@PathVariable int id){
        StatusResponse<FoundsDTO> response = foundsService.deleteFound(id);
        if("Success".equals(response.getStatus())){
            return ResponseEntity.ok(response);
        }else if("Found not found".equals(response.getMessage())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    //Tong chi tieu 1 ngay cu the truyen param (Date)
    @GetMapping("/daily-summary")
    public ResponseEntity<StatusResponse<Map<String, BigDecimal>>> getDailySummary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        StatusResponse<Map<String, BigDecimal>> response = foundsService.getDailySummary(date);
        if ("success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
//Tong chi tieu hom nay
    @GetMapping("/today-summary")
    public ResponseEntity<StatusResponse<Map<String, BigDecimal>>> getTodaySummary() {
        StatusResponse<Map<String, BigDecimal>> response = foundsService.getTodaySummary();
        if ("success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
