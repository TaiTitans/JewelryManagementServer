package com.jewelrymanagement.controller;

import com.jewelrymanagement.dto.FoundDTO;
import com.jewelrymanagement.repository.FoundRepository;
import com.jewelrymanagement.service.FoundsService;
import com.jewelrymanagement.util.StatusResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FoundsController {
    @Autowired
    private FoundsService foundsService;
    @Autowired
    private FoundRepository foundRepository;

    @GetMapping("/common/found")
    public ResponseEntity<StatusResponse<List<FoundDTO>>> getAllFound() {
        StatusResponse<List<FoundDTO>> response = foundsService.getAllFounds();
        if ("Success".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @GetMapping("/common/found/{id}")
    public ResponseEntity<StatusResponse<FoundDTO>> getFoundById(@PathVariable int id) {
        StatusResponse<FoundDTO> response = foundsService.getFoundById(id);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else if ("Found not found".equals(response.getMessage())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping("/manager/found")
    public ResponseEntity<StatusResponse<FoundDTO>> createFound(@Valid @RequestBody FoundDTO foundDTO) {
        StatusResponse<FoundDTO> response = foundsService.createFound(foundDTO);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/manager/found/{id}")
    public ResponseEntity<StatusResponse<FoundDTO>> updateFound(@PathVariable int id, @RequestBody FoundDTO foundDTO) {
        StatusResponse<FoundDTO> response = foundsService.updateFound(id, foundDTO);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else if("Found not found".equals(response.getMessage())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/manager/found/{id}")
public ResponseEntity<StatusResponse<FoundDTO>> deleteFound (@PathVariable int id){
        StatusResponse<FoundDTO> response = foundsService.deleteFound(id);
        if("Success".equals(response.getStatus())){
            return ResponseEntity.ok(response);
        }else if("Found not found".equals(response.getMessage())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    //Tong chi tieu 1 ngay cu the truyen param (Date)
    @GetMapping("/manager/found/daily-summary")
    public ResponseEntity<StatusResponse<Map<String, BigDecimal>>> getDailySummary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            date = LocalDate.now(); // Sử dụng ngày hiện tại nếu tham số date không được cung cấp
        }
        StatusResponse<Map<String, BigDecimal>> response = foundsService.getDailySummary(date);
        if ("success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
//Tong chi tieu hom nay
    @GetMapping("/manager/found/today-summary")
    public ResponseEntity<StatusResponse<Map<String, BigDecimal>>> getTodaySummary() {
        StatusResponse<Map<String, BigDecimal>> response = foundsService.getTodaySummary();
        if ("success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
