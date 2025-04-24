package com.novely.novely.dto;

import java.io.Serializable;

import com.novely.novely.domain.Novel;
import com.novely.novely.enums.Genre;

import jakarta.validation.constraints.Size;

public class NovelUpdateDTO implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Size(max = 50, message = "Title must be at most 50 characters")
    private String title;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;
    
    private Genre genre;

    public NovelUpdateDTO() {

    }

    public NovelUpdateDTO(Novel obj) {
        this.title = obj.getTitle();
        this.description = obj.getDescription();
        this.genre = obj.getGenre();
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
