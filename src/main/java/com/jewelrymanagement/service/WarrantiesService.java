package com.jewelrymanagement.service;

import com.jewelrymanagement.dto.WarrantiesDTO;
import com.jewelrymanagement.entity.Customer;
import com.jewelrymanagement.entity.Warranties;
import com.jewelrymanagement.exceptions.WarrantyStatus;
import com.jewelrymanagement.model.StatusResponse;
import com.jewelrymanagement.repository.CustomerRepository;
import com.jewelrymanagement.repository.ProductRepository;
import com.jewelrymanagement.repository.WarrantiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jewelrymanagement.entity.Product;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WarrantiesService {
    @Autowired
    private WarrantiesRepository warrantiesRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomerRepository customerRepository;


    public WarrantiesDTO convertToDTO(Warranties warranties) {
        WarrantiesDTO warrantiesDTO = new WarrantiesDTO();
        warrantiesDTO.warranty_id = warranties.getWarranty_id();
        warrantiesDTO.description = warranties.getDescription();
        warrantiesDTO.warranty_status = warranties.getWarranty_status();
        warrantiesDTO.request_date = warranties.getRequest_date();
        warrantiesDTO.resolved_date = warranties.getResolved_date();
        warrantiesDTO.customer_id = warranties.getCustomer().getCustomer_id();
        warrantiesDTO.product_id = warranties.getProduct().getProduct_id();
        return warrantiesDTO;
    }

    public Warranties convertToEntity(WarrantiesDTO warrantiesDTO) {
        Warranties warranties = new Warranties();
        warranties.setWarranty_id(warrantiesDTO.warranty_id);
        warranties.setDescription(warrantiesDTO.description);
        warranties.setRequest_date(warrantiesDTO.request_date);
        warranties.setResolved_date(warrantiesDTO.resolved_date);
        warranties.setWarranty_status(warrantiesDTO.warranty_status);
        Product product = productRepository.findById(warrantiesDTO.product_id).orElseThrow();
        warranties.setProduct(product);
        Customer customer = customerRepository.findById(warrantiesDTO.customer_id).orElseThrow();
        warranties.setCustomer(customer);
        return warranties;
    }

    public StatusResponse<List<WarrantiesDTO>> getAllWarranty() {
        try {
            List<Warranties> warranties = warrantiesRepository.findAll();
            List<WarrantiesDTO> warrantiesDTOs = warranties.stream().map(this::convertToDTO).collect(Collectors.toList());
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Warranty list retrieved successfully", warrantiesDTOs);
        } catch (Exception e) {
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "An unexpected error occurred", null);
        }
    }

    public StatusResponse<WarrantiesDTO> getWarrantyById(Integer id) {
        try{
            Optional<Warranties> warrantiesOptional = warrantiesRepository.findById(id);
            if(warrantiesOptional.isPresent()){
                Warranties warranties = warrantiesOptional.get();
                WarrantiesDTO warrantiesDTO = convertToDTO(warranties);
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Warranty retrieved successfully", warrantiesDTO);
            }else {
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Warranty not found", null);
            }
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "An unexpected error occurred", null);
        }
    }
    public StatusResponse<WarrantiesDTO> deleteWarrantyById(Integer id) {
        try{
            warrantiesRepository.deleteById(id);
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Warranty delete successfully", null);
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Delete error", null);
        }
    }

    public WarrantiesDTO updateWarrantyStatus(Integer id, WarrantyStatus newStatus) {
        Warranties warranties = warrantiesRepository.findById(id).orElseThrow();
        warranties.setWarranty_status(newStatus);
        warrantiesRepository.save(warranties);
        WarrantiesDTO warrantiesDTO = convertToDTO(warranties);
        return warrantiesDTO;
    }
    public StatusResponse<List<WarrantiesDTO>> getAllWarrantiesByCustomerId(Integer id){
        try{
            List<Warranties> warrantiesList = warrantiesRepository.findWarrantyByCustomerId(id);
            List<WarrantiesDTO> warrantiesDTOs = warrantiesList.stream().map(this::convertToDTO).collect(Collectors.toList());
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Warranties list retrieved successfully", warrantiesDTOs);
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "An unexpected error occurred", null);
        }
    }

    public StatusResponse<WarrantiesDTO> deleteAllWarrantiesOfCustomerId(Integer id){
        try{
            warrantiesRepository.deleteWarrantiesByCustomer(id);
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Warranties delete successfully", null);
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Delete error", null);
        }
    }

    //Add
public StatusResponse<WarrantiesDTO> addWarranties(WarrantiesDTO warrantiesDTO){
        try{
            Product productCheck = productRepository.findById(warrantiesDTO.product_id).orElseThrow(()->new Exception("Product not found"));
            Customer customerCheck = customerRepository.findById(warrantiesDTO.customer_id).orElseThrow(()->new Exception("Customer not found"));
            Warranties warranties = convertToEntity(warrantiesDTO);
            warranties.setRequest_date(new Date());
            warranties.setResolved_date(new Date());
            warranties.setWarranty_status(WarrantyStatus.valueOf("WAITING"));
            warrantiesRepository.save(warranties);
            WarrantiesDTO warrantiesSave = convertToDTO(warranties);
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Warranty created successfully", warrantiesSave);

        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Create error: " + e.getMessage(), null);
        }
}

    //Update
public StatusResponse<WarrantiesDTO> updateWarranties(Integer id,WarrantiesDTO warrantiesDTO){
        try{
            if(warrantiesRepository.existsById(id)){
                Warranties warranties = warrantiesRepository.findById(id).orElseThrow(()->new Exception("Warranties not found"));
                Warranties warrantiesUpdate = convertToEntity(warrantiesDTO);
                warrantiesUpdate.setWarranty_id(id);
                warrantiesUpdate.setResolved_date(new Date());
                warrantiesRepository.save(warrantiesUpdate);
                WarrantiesDTO warrantiesSave = convertToDTO(warrantiesUpdate);
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Order update successfully", warrantiesSave);
            }else{
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Warranty not found", null);
            }
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Update error", null);
        }
}


}
