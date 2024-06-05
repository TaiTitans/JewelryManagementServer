package com.jewelrymanagement.controller;

import com.jewelrymanagement.dto.UserDTO;
import com.jewelrymanagement.entity.LoginRequest;
import com.jewelrymanagement.entity.LoginResponse;
import com.jewelrymanagement.service.UserService;
import com.jewelrymanagement.util.StatusResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UserService userService;
    @Value("${jwt.access-token-expiration-in-ms}")
    private long accessTokenExpirationInMs;

    @GetMapping("/manager/users")
    public ResponseEntity<StatusResponse<List<UserDTO>>> getAllUsers(){
        StatusResponse<List<UserDTO>> response = userService.getAllUsers();
        if("Success".equals(response.getStatus())){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/manager/user/{id}")
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


    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO){
        StatusResponse<UserDTO> response = userService.createUser(userDTO);
        if ("Success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/user/{id}")
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
    @DeleteMapping("/user/{id}")
    public ResponseEntity<StatusResponse> deleteUser(@PathVariable int id) {
        StatusResponse response = userService.deleteUser(id);
        if("Success".equals(response.getStatus())){
            return ResponseEntity.ok(response);
        } else if("User not found".equals(response.getMessage())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<StatusResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        StatusResponse<LoginResponse> statusResponse = userService.login(loginRequest);
        if ("Success".equals(statusResponse.getStatus())) {
            // Lưu access token và refresh token vào cookie
            LoginResponse loginResponse = statusResponse.getData();
            Cookie accessTokenCookie = new Cookie("accessToken", loginResponse.getAccessToken());
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setSecure(true);
            accessTokenCookie.setPath("/");
            accessTokenCookie.setMaxAge((int) (accessTokenExpirationInMs / 1000));
            response.addCookie(accessTokenCookie);

            Cookie refreshTokenCookie = new Cookie("refreshToken", loginResponse.getRefreshToken());
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge((int) (accessTokenExpirationInMs * 7 / 1000));
            response.addCookie(refreshTokenCookie);

            return ResponseEntity.ok(statusResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(statusResponse);
        }
    }



}
