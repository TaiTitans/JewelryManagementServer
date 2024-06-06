package com.jewelrymanagement.dto;


import java.util.Set;

public class ProductDTO {
    public ProductDTO(Integer product_id, String product_name, Integer supplier_id, Set<ProductDetailsDTO> productDetails) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.supplier_id = supplier_id;
        this.productDetails = productDetails;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Set<ProductDetailsDTO> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(Set<ProductDetailsDTO> productDetails) {
        this.productDetails = productDetails;
    }

    public Integer getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(Integer supplier_id) {
        this.supplier_id = supplier_id;
    }
    public ProductDTO() {
    }
    private Integer product_id;
    private String product_name;
    private Integer supplier_id;
    private Set<ProductDetailsDTO> productDetails;
}
