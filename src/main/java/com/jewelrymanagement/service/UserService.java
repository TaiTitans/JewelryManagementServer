package com.jewelrymanagement.service;
import com.jewelrymanagement.dto.UserDTO;
import com.jewelrymanagement.entity.LoginRequest;
import com.jewelrymanagement.entity.LoginResponse;
import com.jewelrymanagement.entity.User;
import com.jewelrymanagement.exceptions.User.Role;
import com.jewelrymanagement.repository.GetUsernameRepository;
import com.jewelrymanagement.repository.UserRepository;
import com.jewelrymanagement.util.JwtTokenProvider;
import com.jewelrymanagement.util.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class UserService {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GetUsernameRepository getUsernameRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Value("${jwt.access-token-expiration-in-ms}")
    private long accessTokenExpirationInMs;
    public StatusResponse<List<UserDTO>> getAllUsers(){
       try{
           List<User> users = userRepository.findAll();
           List<UserDTO> userDTOs = users.stream().map(this::convertToDto).collect(Collectors.toList());
           return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success", "User retrieved successfully",userDTOs);
       }catch (Exception ex){
           return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", "An unexpected error occurred", null);
       }
    }

    public StatusResponse<UserDTO> getUserById(int id) {
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                UserDTO userDTO = convertToDto(user);
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "User found successfully", userDTO);
            } else {
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "User not found", null);
            }
        } catch (Exception ex) {
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "An unexpected error occurred", null);
        }
    }


public StatusResponse<UserDTO> createUser(UserDTO userDTO){
        try{
            if(getUsernameRepository.findByUsername(userDTO.Username).isPresent()){
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", "Username already exists", null);
            }
            User user = convertToEntity(userDTO);
            user.setPassword(passwordEncoder.encode(userDTO.Password));
            user = userRepository.save(user);
            UserDTO createdUserDTO = convertToDto(user);
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success", "User created successfully", createdUserDTO);
        }catch(Exception ex){
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error","An unexpected error occurred",null );
        }
}
public StatusResponse<UserDTO> updateUser(int id, UserDTO userDTO) {
    try {
       if(userRepository.existsById(id)){
           User user = convertToEntity(userDTO);
           user.getUser_id();
           if(userDTO.Password!=null && !userDTO.Password.isEmpty()){
               user.setPassword(passwordEncoder.encode(userDTO.Password));
           }
           user = userRepository.save(user);
           UserDTO updatedUserDTO = convertToDto(user);
           return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success", "User updated successfully", updatedUserDTO);
       }else{
           return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", "User not found", null);
       }

    }catch (Exception ex){
        return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", "An unexpected error occurred", null);
    }
}
    private UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.user_id = user.getUser_id();
        userDTO.Username = user.getUsername();
        userDTO.Role = Role.valueOf(user.getRole().name());
        return userDTO;
    }
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.Username);
        user.setRole(com.jewelrymanagement.exceptions.User.Role.valueOf(userDTO.Role.name()));
        return user;
    }
    public StatusResponse<UserDTO> deleteUser(int id) {
        try {
            userRepository.deleteById(id);
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success", "User deleted successfully", null);
        } catch (Exception ex) {
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", "Failed to delete user", null);
        }
    }
    public StatusResponse<LoginResponse> login(LoginRequest loginRequest) {
        try {
            Optional<User> userOptional = getUsernameRepository.findByUsername(loginRequest.getUsername());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                    UserDTO userDTO = convertToDto(user);
                    String accessToken = jwtTokenProvider.generateAccessToken(userDTO);
                    String refreshToken = jwtTokenProvider.generateRefreshToken(userDTO);
                    LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken);
                    return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "Login successful", loginResponse);
                } else {
                    return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Invalid username or password", null);
                }
            } else {
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "User not found", null);
            }
        } catch (Exception ex) {
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", ex.getMessage(), null);
        }
    }

}
