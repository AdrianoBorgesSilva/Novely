package com.novely.novely.dto;

import java.io.Serializable;

import com.novely.novely.domain.Chapter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChapterCreateDTO implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 30, message = "Title must be at most 30 characters")
    private String title;

    @NotBlank(message = "Content cannot be blank")
    @Size(max = 5000, message = "Content must be at most 5000 characters")
    private String content;

    @Min(value = 1, message = "Chapter number must be greater than 0")
    private int chapterNumber; 

    public ChapterCreateDTO() {

    }

    public ChapterCreateDTO(Chapter obj) {
        this.title = obj.getTitle();
        this.content = obj.getContent();
        this.chapterNumber = obj.getChapterNumber();
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }
}
