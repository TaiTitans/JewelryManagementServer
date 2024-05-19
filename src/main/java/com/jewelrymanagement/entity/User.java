package com.jewelrymanagement.entity;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IDUser;
    private String Name;
    private byte Sex;
    private String Phone;
    private String Address;
    private String Password;
    private String Role;
    private int Point;
}
