package com.jewelrymanagement.service;

import com.jewelrymanagement.dto.ProductDTO;
import com.jewelrymanagement.entity.Product;
import com.jewelrymanagement.entity.ProductDetails;
import com.jewelrymanagement.entity.Supplier;
import com.jewelrymanagement.repository.ProductRepository;
import com.jewelrymanagement.repository.SupplierRepository;
import com.jewelrymanagement.model.StatusResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ModelMapper modelMapper;

    public ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProduct_id(product.getProduct_id());
        productDTO.setProduct_name(product.getProduct_name());
        productDTO.setSupplier_id(product.getSupplier_id().getSupplier_id());
        return productDTO;
    }

    public Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setProduct_id(productDTO.getProduct_id());
        product.setProduct_name(productDTO.getProduct_name());
        Supplier supplier = supplierRepository.findById(productDTO.getSupplier_id()).orElseThrow();
        product.setSupplier_id(supplier);


        return product;
    }



    public StatusResponse<List<ProductDTO>> getAllProduct(){
        try{
            List<Product> productList = productRepository.findAll();
            List<ProductDTO> productDTOList = productList.stream().map(this::convertToDTO).collect(Collectors.toList());
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success","Product list retrieved successfully", productDTOList);
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error","An unexpected error occurred",null);
        }
}


    public StatusResponse<ProductDTO> getProductById(Integer id) {
        try {
            Optional<Product> productOptional = productRepository.findById(id);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                ProductDTO productDTO = convertToDTO(product);
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Product retrieved successfully", productDTO);
            } else{
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Product not found", null);
            }
        } catch (Exception e) {
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "An unexpected error occurred", null);
        }
    }

    public StatusResponse<ProductDTO> addProduct(ProductDTO productDTO) {
        try {
            Product product = convertToEntity(productDTO);
            product = productRepository.save(product);
            ProductDTO productDTOs = convertToDTO(product);
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Product create successfully", productDTOs);
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Create error", null);
        }
    }

    public StatusResponse<ProductDTO> updateProduct(Integer id,ProductDTO productDTO) {
        try{
            if(productRepository.existsById(id)){
                Product product = convertToEntity(productDTO);
                product.setProduct_id(id);
                Set<ProductDetails> currentProductDetails = product.getProductDetails();
                product.setProductDetails(currentProductDetails);
                product = productRepository.save(product);
                ProductDTO productDTOs = convertToDTO(product);
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Product update successfully", productDTOs);
            }else{
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "ID Product exits", null);
            }
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Update error", null);
        }
    }

    public StatusResponse<ProductDTO> deleteProduct(Integer id) {
        try{
            productRepository.deleteById(id);
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Product delete successfully", null);
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Delete error", null);
        }
    }


}
