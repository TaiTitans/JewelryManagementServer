package com.jewelrymanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.jewelrymanagement.exceptions.User.Role;


@Data
@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    private String Username;

    private String Password;

    @Enumerated(EnumType.STRING)
    private Role Role;

}
