package com.jewelrymanagement.service;


import com.jewelrymanagement.dto.CustomerDTO;
import com.jewelrymanagement.entity.Customer;
import com.jewelrymanagement.repository.CustomerRepository;
import com.jewelrymanagement.util.StatusResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ModelMapper modelMapper;

    public CustomerDTO convertToDTO(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }

    public Customer convertToEntity(CustomerDTO customerDTO) {
        return modelMapper.map( customerDTO, Customer.class);
    }


    public StatusResponse<List<CustomerDTO>> getAllCustomer(){
        try{
            List<Customer> customerList = customerRepository.findAll();
            List<CustomerDTO> customerDTOList = customerList.stream().map(this::convertToDTO).collect(Collectors.toList());
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success","Customer list retrieved successfully", customerDTOList);
        } catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error","An unexpected error occurred",null);
        }
    }

    public StatusResponse<CustomerDTO> getCustomerById(int id) {
        try {
            Optional<Customer> customerOptional = customerRepository.findById(id);
            if (customerOptional.isPresent()) {
                Customer customer = customerOptional.get();
                CustomerDTO customerDTO = convertToDTO(customer);
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Customer retrieved successfully", customerDTO);
            } else {
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Customer not found", null);
            }
        } catch (Exception e) {
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "An unexpected error occurred", null);
        }
    }

public StatusResponse<CustomerDTO> createCustomer(CustomerDTO customerDTO) {
        try{
          Customer customer = convertToEntity(customerDTO);
          customer = customerRepository.save(customer);
          CustomerDTO customerDTOS = convertToDTO(customer);
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Customer create successfully", customerDTOS);
        } catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Create error", null);
        }
}
public StatusResponse<CustomerDTO> updateCustomer(int id, CustomerDTO customerDTO) {
        try{
            if(customerRepository.existsById(id)){
             Customer customer = convertToEntity(customerDTO);
             customer.setCustomer_id(id);
             customer = customerRepository.save(customer);
             CustomerDTO customerDTOS = convertToDTO(customer);
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Customer update successfully", customerDTOS);
            }else{
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "ID Customer exits", null);
            }
        }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Update error", null);
        }
}

public StatusResponse<CustomerDTO> deleteCustomer(int id){
        try{
                customerRepository.deleteById(id);
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Customer delete successfully", null);
            }catch (Exception e){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Delete error", null);
        }
}



}
