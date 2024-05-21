package com.jewelrymanagement.entity;

public class LoginRequest {


    public LoginRequest(String phone, String password) {
        Phone = phone;
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    private String Phone;
    private String Password;
}
