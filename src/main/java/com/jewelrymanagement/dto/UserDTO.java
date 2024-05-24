package com.jewelrymanagement.dto;
import com.jewelrymanagement.exceptions.User.Role;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
public class UserDTO {
    @Id
    public int user_id;
    public String Username;
    public String Password;
    public Role Role;

}
