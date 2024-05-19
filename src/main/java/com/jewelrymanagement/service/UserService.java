package com.jewelrymanagement.service;
import com.jewelrymanagement.dto.UserDTO;
import com.jewelrymanagement.entity.User;
import com.jewelrymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public List<UserDTO> getAllUsers(){
        return userRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public UserDTO getUserById(int id){
        return userRepository.findById(id).map(this::convertToDto).orElse(null);
    }

   public UserDTO createUser(UserDTO userDTO){
        User user = convertToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.Password));
        user = userRepository.save(user);
        return convertToDto(user);
   }
public UserDTO updateUser(int id, UserDTO userDTO){
        if(userRepository.existsById(id)){
            User user = convertToEntity(userDTO);
            user.setIDUser(id);
            if(!userDTO.Password.isEmpty()){
                user.setPassword(passwordEncoder.encode(userDTO.Password));
            }
            user = userRepository.save(user);
            return convertToDto(user);
        }
        return null;
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
    public void deleteUser(int id){
        userRepository.deleteById(id);
    }


}
