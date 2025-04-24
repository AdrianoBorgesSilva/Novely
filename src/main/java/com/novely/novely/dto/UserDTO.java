package com.novely.novely.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.novely.novely.domain.User;

public class UserDTO implements Serializable{
    
    private static final long serialVersionUID = 1L; 

    private String id;

    private String name;
    private String email;
    private String password;
    private String avatarUrl;
    private LocalDate createdAt;

    private boolean deleted;

    private Set<String> favorites;
    private Set<String> superFavorites;

    private List<String> novelsId;
    private List<String> ratingsId;
    private List<String> commentsId;

    public UserDTO() {

    }

    public UserDTO(User obj) {
        this.id = obj.getId();
        this.name = obj.getName();
        this.email = obj.getEmail();
        this.password = obj.getPassword();
        this.avatarUrl = obj.getAvatarUrl();
        this.createdAt = obj.getCreatedAt();
        this.favorites = obj.getFavorites();
        this.superFavorites = obj.getFavorites();
        this.novelsId = obj.getNovelsId();
        this.ratingsId = obj.getRatingsId();
        this.commentsId = obj.getCommentsId();
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Set<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<String> favorites) {
        this.favorites = favorites;
    }

    public Set<String> getSuperFavorites() {
        return superFavorites;
    }

    public void setSuperFavorites(Set<String> superFavorites) {
        this.superFavorites = superFavorites;
    }

    public List<String> getNovelsId() {
        return novelsId;
    }

    public void setNovelsId(List<String> novelsId) {
        this.novelsId = novelsId;
    }

    public List<String> getRatingsId() {
        return ratingsId;
    }

    public void setRatingsId(List<String> ratingsId) {
        this.ratingsId = ratingsId;
    }

    public List<String> getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(List<String> commentsId) {
        this.commentsId = commentsId;
    }
}
