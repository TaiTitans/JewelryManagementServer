package com.jewelrymanagement.service;

import com.jewelrymanagement.dto.FoundDTO;
import com.jewelrymanagement.entity.Found;
import com.jewelrymanagement.exceptions.Found.TransactionType;
import com.jewelrymanagement.repository.FoundRepository;
import com.jewelrymanagement.repository.UserRepository;
import com.jewelrymanagement.model.StatusResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
    @Autowired
    private UserRepository userRepository;

    private Found convertToEntity(FoundDTO foundDTO){
        Found found = new Found();
        found.setAmount(foundDTO.getAmount());
        found.setDescription(foundDTO.getDescription());
        found.setTransactionType(com.jewelrymanagement.exceptions.Found.TransactionType.valueOf(foundDTO.getTransactionType().name()));
        found.setTransactionDate(foundDTO.getTransactionDate());
       found.setCreated_by(foundDTO.getCreated_by());
        return found;
    }

    private FoundDTO convertToDTO(Found found){
        FoundDTO foundDTO = new FoundDTO();
        foundDTO.setFoundID(found.getFoundId());
        foundDTO.setAmount(found.getAmount());
        foundDTO.setDescription(found.getDescription());
        foundDTO.setTransactionType(com.jewelrymanagement.exceptions.Found.TransactionType.valueOf(found.getTransactionType().name()));
        foundDTO.setTransactionDate(found.getTransactionDate());
        foundDTO.setCreated_by(found.getCreated_by());
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

public StatusResponse<FoundDTO> createFound(FoundDTO foundDTO, HttpServletRequest request){
    try{
        String username = getUsernameFromCookie(request);
        Found found = convertToEntity(foundDTO);

        found.setTransactionDate(LocalDate.now());
        found.setCreated_by(username);

        found = foundRepository.save(found);
        FoundDTO createdFoundDTO = convertToDTO(found);
        return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success", "Found created successfully", createdFoundDTO);
    }catch (Exception ex){
        return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", "Found created error", null);
    }
}

public StatusResponse<FoundDTO> updateFound(int id,FoundDTO foundsDTO, HttpServletRequest request){
    try{
        String username = getUsernameFromCookie(request);
        if(foundRepository.existsById(id)){
            Found found = convertToEntity(foundsDTO);
            found.setFoundId(id);
            found.setCreated_by(username);
            found.setTransactionDate(LocalDate.now());
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
//Xem loi nhuan 1 ngay cu the
public StatusResponse<Map<String, BigDecimal>> getDailySummary(LocalDate date){
    try{
        Map<String, BigDecimal> summary = new HashMap<>();
        BigDecimal income = foundRepository.DailySummary(date, TransactionType.IN);
        BigDecimal expenditure = foundRepository.DailySummary(date, TransactionType.OUT);
        // Tính toán tổng lợi nhuận (profit)
        BigDecimal profit = income.subtract(expenditure);
        summary.put("income", income != null ? income : BigDecimal.ZERO);
        summary.put("expenditure", expenditure != null ? expenditure : BigDecimal.ZERO);
        summary.put("profit", profit != null ? profit : BigDecimal.ZERO);
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

            Map<String, BigDecimal> summary = calculateSummary(foundList);

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
    private static Map<String, BigDecimal> calculateSummary(List<Found> foundsList) {
        Map<String, BigDecimal> summary = new HashMap<>();

        BigDecimal income = foundsList.stream()
                .filter(f -> f.getTransactionType() == TransactionType.IN)
                .map(Found::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expenditure = foundsList.stream()
                .filter(f -> f.getTransactionType() == TransactionType.OUT)
                .map(Found::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal profit = income.subtract(expenditure);

        summary.put("income", income);
        summary.put("expenditure", expenditure);
        summary.put("profit", profit);

        return summary;
    }


    private String getUsernameFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
