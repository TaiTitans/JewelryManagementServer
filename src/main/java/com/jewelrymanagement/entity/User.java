package com.jewelrymanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IDUser;

    @NotBlank
    private String Name;
    @NotNull
    private byte Sex;

    @NotBlank
    private String Phone;

    @NotBlank
    private String Address;

    @NotBlank
    private String Password;

    @NotBlank
    @Pattern(regexp = "customer|staff", message = "Role must be either 'customer' or 'staff'")
    private String Role;

    private int Point;
}
