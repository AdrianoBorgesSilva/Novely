package com.novely.novely.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class User implements Serializable{
    
    private static final long serialVersionUID = 1L; 

    @Id
    private String id;
    
    private String name;
    private String email;
    private String password;
    private String avatarUrl;
    private LocalDate createdAt;

    private boolean deleted = false;

    private Set<String> favorites = new LinkedHashSet<>();
    private Set<String> superFavorites = new LinkedHashSet<>();

    private List<String> novelsId = new ArrayList<>();
    private List<String> ratingsId = new ArrayList<>();
    private List<String> commentsId = new ArrayList<>();

    public User() {

    }

    public User(String id, String name, String email, String password, String avatarUrl, LocalDate createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
