package com.jewelrymanagement.service;
import com.jewelrymanagement.dto.UserDTO;
import com.jewelrymanagement.entity.User;
import com.jewelrymanagement.repository.UserRepository;
import com.jewelrymanagement.util.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class UserService {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public StatusResponse<List<UserDTO>> getAllUsers(){
       try{
           List<User> users = userRepository.findAll();
           List<UserDTO> userDTOs = users.stream().map(this::convertToDto).collect(Collectors.toList());
           return new StatusResponse<>("Success", "User retrieved successfully",userDTOs);
       }catch (Exception ex){
           return new StatusResponse<>("Error", "An unexpected error occurred", null);
       }
    }

    public StatusResponse<UserDTO> getUserById(int id) {
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                UserDTO userDTO = convertToDto(user);
                return new StatusResponse<>("Success", "User found successfully", userDTO);
            } else {
                return new StatusResponse<>("Error", "User not found", null);
            }
        } catch (Exception ex) {
            return new StatusResponse<>("Error", "An unexpected error occurred", null);
        }
    }


    public UserDTO createUser(UserDTO userDTO){
        User user = convertToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.Password));
        user = userRepository.save(user);
        return convertToDto(user);
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
           return new StatusResponse<>("Success", "User updated successfully", updatedUserDTO);
       }else{
           return new StatusResponse<>("Error", "User not found", null);
       }

    }catch (Exception ex){
        return new StatusResponse<>("Error", "An unexpected error occurred", null);
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
            return new StatusResponse<>("Success", "User deleted successfully", null);
        } catch (Exception ex) {
            return new StatusResponse<>("Error", "Failed to delete user", null);
        }
    }


}
