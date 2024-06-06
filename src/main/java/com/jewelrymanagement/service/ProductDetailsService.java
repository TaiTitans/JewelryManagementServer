package com.jewelrymanagement.service;

import com.jewelrymanagement.dto.ProductDetailsDTO;
import com.jewelrymanagement.entity.Product;
import com.jewelrymanagement.entity.ProductDetails;
import com.jewelrymanagement.repository.ProductDetailsRepository;
import com.jewelrymanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
