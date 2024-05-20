package com.jewelrymanagement.controller;

import com.jewelrymanagement.dto.UserDTO;
import com.jewelrymanagement.exceptions.User.DeletedSuccess;
import com.jewelrymanagement.exceptions.User.UserNotFoundException;
import com.jewelrymanagement.service.UserService;
import com.jewelrymanagement.util.ErrorResponse;
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
    public List<UserDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id){
        try {
            UserDTO userDTO = userService.getUserById(id);
            return ResponseEntity.ok(userDTO);
        } catch(UserNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(),e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
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
    public UserDTO updateUser(@PathVariable int id, @RequestBody UserDTO userDTO){
        return userService.updateUser(id, userDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            throw new DeletedSuccess(id); // Ném DeletedSuccess để bắt lại thông điệp
        } catch (DeletedSuccess ex) {
            return ResponseEntity.ok(ex.getMessage()); // Trả về thông điệp từ ngoại lệ DeletedSuccess
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

}
