package com.novely.novely.dto;

import java.io.Serializable;

import com.novely.novely.domain.Novel;
import com.novely.novely.enums.Genre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class NovelCreateDTO implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Title is required")
    @Size(max = 50, message = "Title must be at most 50 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @NotNull
    private Genre genre;

    public NovelCreateDTO() {

    }

    public NovelCreateDTO(Novel obj) {
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
