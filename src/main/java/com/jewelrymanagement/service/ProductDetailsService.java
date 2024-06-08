package com.jewelrymanagement.service;

import com.jewelrymanagement.dto.ProductDetailsDTO;
import com.jewelrymanagement.entity.Product;
import com.jewelrymanagement.entity.ProductDetails;
import com.jewelrymanagement.repository.ProductDetailsRepository;
import com.jewelrymanagement.repository.ProductRepository;
import com.jewelrymanagement.model.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductDetailsService {
    @Autowired
    private ProductDetailsRepository productDetailsRepository;
    @Autowired
    private ProductRepository productRepository;

    public ProductDetailsDTO convertToDTO(ProductDetails productDetails) {
        ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO();
        productDetailsDTO.setProductDetailsId(productDetails.getProduct_details_id());
        productDetailsDTO.setProductId(productDetails.getProductId().getProduct_id());
        productDetailsDTO.setDescription(productDetails.getDescription());
        productDetailsDTO.setPrice(productDetails.getPrice());
        productDetailsDTO.setQuantity(productDetails.getQuantity());
        productDetailsDTO.setPoints(productDetails.getPoints());
        productDetailsDTO.setImage(productDetails.getImage());

        return productDetailsDTO;
    }

    public ProductDetails convertToEntity(ProductDetailsDTO productDetailsDTO) {
        ProductDetails productDetails = new ProductDetails();
        productDetails.setProduct_details_id(productDetailsDTO.getProductDetailsId());
        Product product = productRepository.findById(productDetailsDTO.getProductId()).orElseThrow();
        productDetails.setProductId(product);

        productDetails.setDescription(productDetailsDTO.getDescription());
        productDetails.setPrice(productDetailsDTO.getPrice());
        productDetails.setQuantity(productDetailsDTO.getQuantity());
        productDetails.setPoints(productDetailsDTO.getPoints());
        productDetails.setImage(productDetailsDTO.getImage());

        return productDetails;
    }


    public StatusResponse<ProductDetailsDTO> addProductDetails(ProductDetailsDTO productDetailsDTO) {
        try{
            ProductDetails productDetails = convertToEntity(productDetailsDTO);
            productDetailsRepository.save(productDetails);
            ProductDetailsDTO productDetailsDTOS = convertToDTO(productDetails);
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "ProductDetails create successfully", productDetailsDTOS);
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Create error", null);
        }
    }

    public StatusResponse<List<ProductDetailsDTO>> getAllProductById(Integer id){
        try{
            List<ProductDetails> productDetailsList = productDetailsRepository.findProductByProductId(id);
            List<ProductDetailsDTO> productDetailsDTOSave = productDetailsList.stream().map(this::convertToDTO).collect(Collectors.toList());
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Product list retrieved successfully", productDetailsDTOSave);
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "An unexpected error occurred", null);
        }
    }


    public StatusResponse<List<ProductDetailsDTO>> findAllProductDetails() {
        try {
            List<ProductDetails> productDetailsList = productDetailsRepository.findAll();
            List<ProductDetailsDTO> productDetailsDTOList = productDetailsList.stream().map(this::convertToDTO).collect(Collectors.toList());
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Product list retrieved successfully", productDetailsDTOList);
        } catch (Exception e) {
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "An unexpected error occurred", null);
        }
    }

    public StatusResponse<ProductDetailsDTO> getProductDetails(Integer id){
        try{
            Optional<ProductDetails> productDetailsOptional = productDetailsRepository.findById(id);
            if(productDetailsOptional.isPresent()){
                ProductDetails productDetails = productDetailsOptional.get();
                ProductDetailsDTO productDetailsDTO = convertToDTO(productDetails);
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "ProductDetails retrieved successfully", productDetailsDTO);
            }else{
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "ProductDetails not found", null);
            }
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "An unexpected error occurred", null);
        }
    }

    public StatusResponse<ProductDetailsDTO> updateProductDetails(Integer id, ProductDetailsDTO productDetailsDTO) {
        try{
            if(productDetailsRepository.existsById(id)){
                ProductDetails productDetails = convertToEntity(productDetailsDTO);
                productDetails.setProduct_details_id(id);
                productDetailsRepository.save(productDetails);
               ProductDetailsDTO productDetailsDTOSave = convertToDTO(productDetails);
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "ProductDetails update successfully", productDetailsDTOSave);
            }else{
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "ID ProductDetails exits", null);
            }
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "An unexpected error occurred", null);
        }
    }

    public StatusResponse<ProductDetailsDTO> deteteProductDetails(Integer id){
        try{
            productDetailsRepository.deleteById(id);
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "ProductDetails delete successfully", null);
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Delete error", null);
        }
    }

    public StatusResponse<?> deleteProductDetailsByProductId(Integer id) {
        try {
            productDetailsRepository.deleteAllByProductId(id);

            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "ProductDetails delete successfully", null);

        } catch (Exception e) {
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Delete error", null);

        }
    }
}
