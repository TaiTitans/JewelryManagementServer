package com.jewelrymanagement.dto;

import com.jewelrymanagement.exceptions.WarrantyStatus;

import java.util.Date;

public class WarrantiesDTO {
    public Integer warranty_id;
    public String description;
    public Integer product_id;
    public Integer customer_id;
    public WarrantyStatus warranty_status;
    public Date request_date;
    public Date resolved_date;
}
