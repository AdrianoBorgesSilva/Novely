package com.novely.novely.dto;

import java.io.Serializable;

import com.novely.novely.domain.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserSignUpDTO implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 15, message = "Name must be between 3 and 15 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    public UserSignUpDTO() {

    }

    public UserSignUpDTO(User obj) {
        this.name = obj.getName();
        this.email = obj.getEmail();
        this.password = obj.getPassword();
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
