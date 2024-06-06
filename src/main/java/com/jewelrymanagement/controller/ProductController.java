package com.jewelrymanagement.controller;


import com.jewelrymanagement.dto.ProductDTO;
import com.jewelrymanagement.service.ProductService;
import com.jewelrymanagement.util.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/common/product")
    public ResponseEntity<StatusResponse<List<ProductDTO>>> getAllProducts() {
        StatusResponse<List<ProductDTO>> response = productService.getAllProduct();
        if("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/common/product/{id}")
    public ResponseEntity<StatusResponse<ProductDTO>> getProduct(@PathVariable int id) {
        StatusResponse<ProductDTO> response = productService.getProductById(id);
        if("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/manager/product")
    public ResponseEntity<StatusResponse<ProductDTO>> addProduct(@RequestBody ProductDTO productDTO) {
        StatusResponse<ProductDTO> response = productService.addProduct(productDTO);
        if("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PutMapping("/manager/product/{id}")
    public ResponseEntity<StatusResponse<ProductDTO>> updateProduct(@PathVariable int id, @RequestBody ProductDTO productDTO) {
        StatusResponse<ProductDTO> response = productService.updateProduct(id, productDTO);
        if("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/manager/product/{id}")
    public ResponseEntity<StatusResponse<ProductDTO>> deleteProduct(@PathVariable int id) {
        StatusResponse<ProductDTO> response = productService.deleteProduct(id);
        if("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
