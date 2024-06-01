package com.jewelrymanagement.service;
import com.jewelrymanagement.dto.UserDTO;
import com.jewelrymanagement.entity.LoginRequest;
import com.jewelrymanagement.entity.LoginResponse;
import com.jewelrymanagement.entity.Role;
import com.jewelrymanagement.entity.User;
import com.jewelrymanagement.repository.GetUsernameRepository;
import com.jewelrymanagement.repository.RoleRepository;
import com.jewelrymanagement.repository.UserRepository;
import com.jewelrymanagement.util.JwtTokenProvider;
import com.jewelrymanagement.util.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    @Autowired
    private RoleRepository roleRepository;
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


    public StatusResponse<UserDTO> createUser(UserDTO userDTO) {
        try {
            if (getUsernameRepository.findByUsername(userDTO.getUsername()).isPresent()) {
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", "Username already exists", null);
            }

            // Lấy các vai trò từ cơ sở dữ liệu dựa trên tên vai trò trong UserDTO
            Set<Role> roles = new HashSet<>();
            for (String roleName : userDTO.getRoles()) {
                Optional<Role> roleOptional = roleRepository.findByName(roleName);
                if (roleOptional.isPresent()) {
                    roles.add(roleOptional.get());
                } else {
                    return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", "Invalid role: " + roleName, null);
                }
            }

            // Chuyển đổi UserDTO thành entity User
            User user = convertToEntity(userDTO);
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setRoles(roles); // Thiết lập các vai trò cho người dùng

            // Lưu người dùng vào cơ sở dữ liệu
            user = userRepository.save(user);
            UserDTO createdUserDTO = convertToDto(user);

            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Success", "User created successfully", createdUserDTO);
        } catch (Exception ex) {
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),"Error", "An unexpected error occurred", null);
        }
    }
    public StatusResponse<UserDTO> updateUser(int id, UserDTO userDTO) {
        try {
            if (userRepository.existsById(id)) {
                // Lấy người dùng hiện tại từ cơ sở dữ liệu
                Optional<User> optionalUser = userRepository.findById(id);
                if (!optionalUser.isPresent()) {
                    return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "User not found", null);
                }
                User user = optionalUser.get();

                // Kiểm tra vai trò
                Set<Role> roles = new HashSet<>();
                for (String roleName : userDTO.getRoles()) {
                    Optional<Role> roleOptional = roleRepository.findByName(roleName);
                    if (roleOptional.isPresent()) {
                        roles.add(roleOptional.get());
                    } else {
                        return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Invalid role: " + roleName, null);
                    }
                }

                // Cập nhật thông tin người dùng từ UserDTO
                user.setUsername(userDTO.getUsername());
                if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                }
                user.setRoles(roles); // Thiết lập các vai trò cho người dùng

                // Lưu người dùng vào cơ sở dữ liệu
                user = userRepository.save(user);
                UserDTO updatedUserDTO = convertToDto(user);

                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "User updated successfully", updatedUserDTO);
            } else {
                return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "User not found", null);
            }

        } catch (Exception ex) {
            return new StatusResponse<>(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "An unexpected error occurred", null);
        }
    }

    private UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUser_id(user.getUser_id());
        userDTO.setUsername(user.getUsername());
        Set<String> roles = new HashSet<>();
        for (Role role : user.getRoles()) {
            roles.add(role.getName());
        }
        userDTO.setRoles(roles);
        return userDTO;
    }
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        Set<Role> roles = new HashSet<>();
        for (String roleName : userDTO.getRoles()) {
            Role role = new Role();
            role.setName(roleName);
            roles.add(role);
        }
        user.setRoles(roles);
        return user;
    }
    public StatusResponse deleteUser(int id) {
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                // Xóa tất cả các vai trò liên quan của người dùng
                user.getRoles().clear();
                userRepository.save(user);

                // Sau đó xóa người dùng
                userRepository.deleteById(id);

                return new StatusResponse(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Success", "User deleted successfully", null);
            } else {
                return new StatusResponse(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "User not found", null);
            }
        } catch (Exception ex) {
            return new StatusResponse(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), "Error", "Failed to delete user: " + ex.getMessage(), null);
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
