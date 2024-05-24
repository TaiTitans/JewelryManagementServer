package com.jewelrymanagement.service;

import com.jewelrymanagement.dto.FoundDTO;
import com.jewelrymanagement.entity.Found;
import com.jewelrymanagement.exceptions.Found.TransactionType;
import com.jewelrymanagement.exceptions.User.Role;
import com.jewelrymanagement.repository.FoundRepository;
import com.jewelrymanagement.util.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FoundsService {
    @Autowired
    private FoundRepository foundRepository;


    private Found convertToEntity(FoundDTO foundDTO){
        Found found = new Found();
        found.setAmount(foundDTO.getAmount());
        found.setDescription(foundDTO.getDescription());
        found.setTransactionType(com.jewelrymanagement.exceptions.Found.TransactionType.valueOf(foundDTO.getTransactionType().name()));
        found.setTransactionDate(foundDTO.getTransactionDate());
        return found;
    }

    private FoundDTO convertToDTO(Found found){
        FoundDTO foundDTO = new FoundDTO();
        foundDTO.setFoundID(found.getFoundId()); // Sử dụng setter mới
        foundDTO.setAmount(found.getAmount()); // Sử dụng setter mới
        foundDTO.setDescription(found.getDescription()); // Sử dụng setter mới
        foundDTO.setTransactionType(com.jewelrymanagement.exceptions.Found.TransactionType.valueOf(found.getTransactionType().name()));
        foundDTO.setTransactionDate(found.getTransactionDate()); // Sử dụng setter mới
        return foundDTO;
    }


    public StatusResponse<List<FoundDTO>> getAllFounds() {
        try {
            List<Found> found = foundRepository.findAll();
            List<FoundDTO> foundsDTOs = found.stream().map(this::convertToDTO).collect(Collectors.toList());
            return new StatusResponse<>(
                    UUID.randomUUID().toString(),
                    LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                    "Success",
                    "User retrieved successfully",
                    foundsDTOs
            );
        } catch (Exception ex) {
            return new StatusResponse<>(
                    UUID.randomUUID().toString(),
                    LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                    "Error",
                    "An unexpected error occurred: " + ex.getMessage(), // Including exception message for better debugging
                    null
            );
        }
    }
public StatusResponse<FoundDTO> getFoundById(int id){
    try{
        Optional<Found> foundOptional = foundRepository.findById(id);
        if(foundOptional.isPresent()){
            Found founds = foundOptional.get();
            FoundDTO foundsDTO = convertToDTO(founds);
            return new StatusResponse<>(UUID.randomUUID().toString(),LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success", "Successfully get found", foundsDTO);
        }else{
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "User not found", null);
        }
    }catch (Exception ex){
        return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "An unexpected error occurred", null);
    }
}

public StatusResponse<FoundDTO> createFound(FoundDTO foundDTO){
    try{
        Found found = convertToEntity(foundDTO);

        found.setTransactionDate(LocalDate.now());


        found = foundRepository.save(found);
        FoundDTO createdFoundDTO = convertToDTO(found);
        return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success", "Found created successfully", createdFoundDTO);
    }catch (Exception ex){
        return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", "Found created error", null);
    }
}

public StatusResponse<FoundDTO> updateFound(int id,FoundDTO foundsDTO){
    try{
        if(foundRepository.existsById(id)){
            Found found = convertToEntity(foundsDTO);
            found.setFoundId(id);
            found = foundRepository.save(found);
            FoundDTO updatedFoundDTO = convertToDTO(found);
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success", "Found updated successfully", updatedFoundDTO);
        } else {
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", "Found not found", null);
        }
    }catch (Exception ex){
        return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", "An Unexpected error occured", null);
    }
}

public StatusResponse<FoundDTO> deleteFound(int id){
    try{
        foundRepository.deleteById(id);
        return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success", "Found deleted successfully", null);
    }catch (Exception ex){
        return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", "Failed to deleted found", null);
    }
}

public StatusResponse<Map<String, BigDecimal>> getDailySummary(LocalDate date){
    try{
        Map<String, BigDecimal> summary = new HashMap<>();
        BigDecimal income = foundRepository.DailySummary(date, TransactionType.IN);
        BigDecimal expenditure = foundRepository.DailySummary(date, TransactionType.OUT);
        summary.put("income", income != null ? income : BigDecimal.ZERO);
        summary.put("expenditure", expenditure != null ? expenditure : BigDecimal.ZERO);
        return new StatusResponse<>(UUID.randomUUID().toString(),LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success", "Daily Summary retrieved successfully", summary);

    }catch (Exception ex){
        return new StatusResponse<>(UUID.randomUUID().toString(),LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", "An unexpected error occured", null
        );
    }
}


    public StatusResponse<Map<String, BigDecimal>> getTodaySummary() {
        try {
            LocalDate today = LocalDate.now();
            List<Found> foundList = foundRepository.findAll().stream()
                    .filter(f -> f.getTransactionDate() != null && f.getTransactionDate().equals(today))
                    .collect(Collectors.toList());
            Map<String, BigDecimal> summary = getTodaySummary(foundList);

            return new StatusResponse<>(
                    UUID.randomUUID().toString(),
                    LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                    "success",
                    "Today's summary retrieved successfully",
                    summary
            );
        } catch (Exception ex) {
            return new StatusResponse<>(
                    UUID.randomUUID().toString(),
                    LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                    "error",
                    ex.getMessage(),
                    null
            );
        }
    }
    private static Map<String, BigDecimal> getTodaySummary(List<Found> foundsList) {
        Map<String, BigDecimal> summary = new HashMap<>();
        summary.put("income", foundsList.stream()
                .filter(f -> f.getTransactionType() == TransactionType.IN)
                .map(Found::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        summary.put("expenditure", foundsList.stream()
                .filter(f -> f.getTransactionType() == TransactionType.OUT)
                .map(Found::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        return summary;
    }




}
