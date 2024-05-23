package com.jewelrymanagement.service;

import com.jewelrymanagement.dto.FoundsDTO;
import com.jewelrymanagement.entity.Founds;
import com.jewelrymanagement.repository.FoundsRepository;
import com.jewelrymanagement.util.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FoundsService {
@Autowired
    private FoundsRepository foundsRepository;

private Founds convertToEntity(FoundsDTO foundsDTO){
    Founds founds = new Founds();
    founds.setAmount(foundsDTO.Amount);
    founds.setDescription(foundsDTO.Description);
    founds.setTransactionType(foundsDTO.TransactionType);
    founds.setTransactionDate(foundsDTO.TransactionDate);
    founds.setCreatedAt(foundsDTO.CreatedAt);
    founds.setUpdatedAt(foundsDTO.UpdatedAt);
    return founds;
}

private FoundsDTO convertToDTO(Founds founds){
    FoundsDTO foundsDTO = new FoundsDTO();
    foundsDTO.Amount = founds.getAmount();
    foundsDTO.Description = founds.getDescription();
    foundsDTO.TransactionType = founds.getTransactionType();
    foundsDTO.TransactionDate = founds.getTransactionDate();
    foundsDTO.CreatedAt = founds.getCreatedAt();
    foundsDTO.UpdatedAt = founds.getUpdatedAt();
    return foundsDTO;
}

public StatusResponse<List<FoundsDTO>> getAllFounds(){
    try{
        List<Founds> founds = foundsRepository.findAll();
        List<FoundsDTO> foundsDTOs = founds.stream().map(this::convertToDTO).collect(Collectors.toList());
        return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success", "User retrieved successfully", foundsDTOs);
    }catch (Exception ex){
        return new StatusResponse<>(UUID.randomUUID().toString(),LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error","An unexpected error occured", null);
    }
}

public StatusResponse<FoundsDTO> getFoundById(int id){
    try{
        Optional<Founds> foundsOptional = foundsRepository.findById(id);
        if(foundsOptional.isPresent()){
            Founds founds = foundsOptional.get();
            FoundsDTO foundsDTO = convertToDTO(founds);
            return new StatusResponse<>(UUID.randomUUID().toString(),LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success", "Successfully get found", foundsDTO);
        }else{
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "User not found", null);
        }
    }catch (Exception ex){
        return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "An unexpected error occurred", null);
    }
}
public StatusResponse<FoundsDTO> createFound(FoundsDTO foundsDTO){
    try{
        Founds founds = convertToEntity(foundsDTO);
        founds = foundsRepository.save(founds);
        FoundsDTO createdFoundDTO = convertToDTO(founds);
        return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success", "Found created successfully", createdFoundDTO);
    }catch (Exception ex){
        return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", "User created error", null);
    }
}

public StatusResponse<FoundsDTO> updateFound(int id,FoundsDTO foundsDTO){
    try{
        if(foundsRepository.existsById(id)){
            Founds found = convertToEntity(foundsDTO);
            found.setFoundID(id);
            found = foundsRepository.save(found);
            FoundsDTO updatedFoundDTO = convertToDTO(found);
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success", "Found updated successfully", updatedFoundDTO);
        } else {
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", "Found not found", null);
        }
    }catch (Exception ex){
        return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", "An Unexpected error occured", null);
    }
}

public StatusResponse<FoundsDTO> deleteFound(int id){
    try{
        foundsRepository.deleteById(id);
        return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success", "Found deleted successfully", null);
    }catch (Exception ex){
        return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", "Failed to deleted found", null);
    }
}

}
