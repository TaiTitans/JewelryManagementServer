package com.jewelrymanagement.controller;

import com.jewelrymanagement.dto.UserDTO;
import com.jewelrymanagement.entity.User;
import com.jewelrymanagement.service.UserService;
import com.jewelrymanagement.util.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UserService userService;
    @GetMapping
    public ResponseEntity<StatusResponse<List<UserDTO>>> getAllUsers(){
        StatusResponse<List<UserDTO>> response = userService.getAllUsers();
        if("Success".equals(response.getStatus())){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusResponse<UserDTO>> getUserById(@PathVariable int id) {
        StatusResponse<UserDTO> response = userService.getUserById(id);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else if ("User not found".equals(response.getMessage())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO){
        try{
            UserDTO response = userService.createUser(userDTO);
            return ResponseEntity.ok(response);
        }catch (ConstraintViolationException ex){
            StringBuilder errorMessage = new StringBuilder();
            ex.getConstraintViolations().forEach(violation -> errorMessage.append(violation.getMessage()).append("; "));
            return ResponseEntity.badRequest().body(errorMessage.toString());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusResponse<UserDTO>> updateUser(@PathVariable int id, @RequestBody UserDTO userDTO){
        StatusResponse<UserDTO> response = userService.updateUser(id, userDTO);
        if("Success".equals(response.getStatus())){
            return ResponseEntity.ok(response);
        }else if("User not found".equals(response.getMessage())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<StatusResponse> deleteUser(@PathVariable int id) {
        StatusResponse response = userService.deleteUser(id);
        if(response.getMessage().equals("Success")){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
