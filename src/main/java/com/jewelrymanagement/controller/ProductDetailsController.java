package com.jewelrymanagement.controller;

import com.jewelrymanagement.dto.ProductDetailsDTO;
import com.jewelrymanagement.service.ProductDetailsService;
import com.jewelrymanagement.util.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductDetailsController {

    @Autowired
    private ProductDetailsService productDetailsService;


    @PostMapping("/manager/productdetails")
    public ResponseEntity<StatusResponse<ProductDetailsDTO>> addProductDetails(@RequestBody ProductDetailsDTO productDetailsDTO) {
        StatusResponse<ProductDetailsDTO> response = productDetailsService.addProductDetails(productDetailsDTO);
        if("Success".equals(response.getStatus())){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/common/productdetails")
    public ResponseEntity<StatusResponse<List<ProductDetailsDTO>>> getAllProductDetails(){
        StatusResponse<List<ProductDetailsDTO>> response = productDetailsService.findAllProductDetails();
        if("Success".equals(response.getStatus())){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/common/productdetails/{id}")
    public ResponseEntity<StatusResponse<ProductDetailsDTO>> getProductDetailsById(@PathVariable int id){
        StatusResponse<ProductDetailsDTO> response = productDetailsService.getProductDetails(id);
        if("Success".equals(response.getStatus())){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/manager/productdetails/{id}")
    public ResponseEntity<StatusResponse<ProductDetailsDTO>> updateProductDetails(@PathVariable int id, @RequestBody ProductDetailsDTO productDetailsDTO){
        StatusResponse<ProductDetailsDTO> response = productDetailsService.updateProductDetails(id, productDetailsDTO);
        if("Success".equals(response.getStatus())){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/manager/productdetails/{id}")
    public ResponseEntity<StatusResponse<ProductDetailsDTO>> deleteProductDetails(@PathVariable int id){
        StatusResponse<ProductDetailsDTO> response = productDetailsService.deteteProductDetails(id);
        if("Success".equals(response.getStatus())){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
