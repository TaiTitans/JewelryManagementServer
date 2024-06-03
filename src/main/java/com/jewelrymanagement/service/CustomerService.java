package com.jewelrymanagement.service;


import com.jewelrymanagement.dto.CustomerDTO;
import com.jewelrymanagement.entity.Customer;
import com.jewelrymanagement.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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



}
