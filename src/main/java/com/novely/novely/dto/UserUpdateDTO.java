package com.novely.novely.dto;

import java.io.Serializable;

import com.novely.novely.domain.User;

import jakarta.validation.constraints.Size;

public class UserUpdateDTO implements Serializable{
    
    private static final long serialVersionUID = 1L; 
   
    @Size(min = 3, max = 15, message = "Name must be between 3 and 15 characters")
    private String name;

    @Size(max = 50, message = "Avatar URL must be at most 50 characters")
    private String avatarUrl;

    public UserUpdateDTO() {

    }

    public UserUpdateDTO(User obj) {
        this.name = obj.getName();
        this.avatarUrl = obj.getAvatarUrl();
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}

