package com.jewelrymanagement.controller;

import com.jewelrymanagement.dto.FoundsDTO;
import com.jewelrymanagement.service.FoundsService;
import com.jewelrymanagement.util.StatusResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/found")
public class FoundsController {
    @Autowired
    private FoundsService foundsService;

    @GetMapping
    public ResponseEntity<StatusResponse<List<FoundsDTO>>> getAllFound() {
        StatusResponse<List<FoundsDTO>> response = foundsService.getAllFounds();
        if ("Success".equals(response.getStatus())) {
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


    @PutMapping
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

}
