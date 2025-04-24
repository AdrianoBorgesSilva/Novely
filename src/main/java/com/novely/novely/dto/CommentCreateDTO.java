package com.novely.novely.dto;

import java.io.Serializable;

import com.novely.novely.domain.Comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CommentCreateDTO implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Content is required")
    @Size(max = 150, message = "Content must be at most 150 characters")
    private String content;

    public CommentCreateDTO() {

    }

    public CommentCreateDTO(Comment obj) {
        this.content = obj.getContent();
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
