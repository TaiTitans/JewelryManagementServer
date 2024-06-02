package com.jewelrymanagement.service;

import com.jewelrymanagement.dto.SupplierDTO;
import com.jewelrymanagement.entity.Supplier;
import com.jewelrymanagement.repository.SupplierRepository;
import com.jewelrymanagement.util.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ModelMapper modelMapper;

    public SupplierDTO convertToDTO(Supplier supplier) {
       return modelMapper.map(supplier, SupplierDTO.class);
    }

    public Supplier convertToEntity(SupplierDTO supplierDTO) {
        return modelMapper.map( supplierDTO, Supplier.class);
    }

    public StatusResponse<List<SupplierDTO>> getAllSuppliers(){
        try{
            List<Supplier> suppliers = supplierRepository.findAll();
            List<SupplierDTO> supplierDTOS = suppliers.stream().map(this::convertToDTO).collect(Collectors.toList());
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success","Supplier list retrieved successfully",supplierDTOS);
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error","An unexpected error occurred",null);
        }
    }

    public StatusResponse<SupplierDTO> getSupplierById(int id){
        try{
            Optional<Supplier> supplierOptional = supplierRepository.findById(id);
            if(supplierOptional.isPresent()) {
                Supplier supplier = supplierOptional.get();
                SupplierDTO supplierDTO = convertToDTO(supplier);
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Supplier retrieved successfully", supplierDTO);
            }else {
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Supplier not found", null);
            }
                } catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "An unexpected error occurred", null);
            }
        }

    public StatusResponse<SupplierDTO> createSupplier(SupplierDTO supplierDTO) {
        try{
            Supplier supplier = convertToEntity(supplierDTO);
            supplier = supplierRepository.save(supplier);

            SupplierDTO createSupplierDTO = convertToDTO(supplier);
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Supplier create successfully", createSupplierDTO);
        } catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Create error", null);
        }
    }
   public StatusResponse<SupplierDTO> updateSupplier(int id, SupplierDTO supplierDTO) {
        try{
            if(supplierRepository.existsById(id)) {
                Supplier supplier = convertToEntity(supplierDTO);
                supplier.setSupplier_id(id);
                supplier = supplierRepository.save(supplier);
                SupplierDTO updatedSupplierDTO = convertToDTO(supplier);
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Supplier update successfully", updatedSupplierDTO);
            } else{
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "ID Supplier not found", null);
            }
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Update error", null);
        }
   }

   public StatusResponse<SupplierDTO> deleteSupplier(int id) {
        try{
            supplierRepository.deleteById(id);
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Supplier delete successfully", null);
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Delete error", null);
        }
   }

    }

