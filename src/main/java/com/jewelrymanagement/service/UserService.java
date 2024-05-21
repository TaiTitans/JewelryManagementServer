package com.jewelrymanagement.service;
import com.jewelrymanagement.dto.UserDTO;
import com.jewelrymanagement.entity.LoginRequest;
import com.jewelrymanagement.entity.LoginResponse;
import com.jewelrymanagement.entity.User;
import com.jewelrymanagement.repository.GetPhoneRepository;
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
    private GetPhoneRepository getPhoneRepository;
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
            if(getPhoneRepository.findByPhone(userDTO.Phone).isPresent()){
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", "Phone number already exists", null);
            }
            User user = convertToEntity(userDTO);
            user.setPassword(passwordEncoder.encode(userDTO.Password));
            user.setPoint(0);
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
           user.setIDUser(id);
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
        userDTO.IDUser = user.getIDUser();
        userDTO.Name = user.getName();
        userDTO.Sex = user.getSex();
        userDTO.Address = user.getAddress();
        userDTO.Phone = user.getPhone();
        userDTO.Role = user.getRole();
        userDTO.Point = user.getPoint();
        return userDTO;
    }
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.Name);
        user.setSex(userDTO.Sex);
        user.setAddress(userDTO.Address);
        user.setPhone(userDTO.Phone);
        user.setRole(userDTO.Role);
        user.setPoint(userDTO.Point);
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
            // Tìm kiếm user dựa trên số điện thoại
            Optional<User> userOptional = getPhoneRepository.findByPhone(loginRequest.getPhone());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                // Kiểm tra mật khẩu
                if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                    // Kiểm tra vai trò của user
                    if (user.getRole().equals("staff")) {
                        // Chuyển đổi User sang UserDTO
                        UserDTO userDTO = convertToDto(user);
                        // Tạo access token và refresh token
                        String accessToken = jwtTokenProvider.generateAccessToken(userDTO);
                        String refreshToken = jwtTokenProvider.generateRefreshToken(userDTO);
                        LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken);
                        return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success", "Login successful", loginResponse);
                    } else {
                        return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", "User is not a staff", null);
                    }
                } else {
                    throw new BadCredentialsException("Invalid username or password");
                }
            } else {
                throw new BadCredentialsException("Invalid username or password");
            }
        } catch (Exception ex) {
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", ex.getMessage(), null);
        }
    }

}
