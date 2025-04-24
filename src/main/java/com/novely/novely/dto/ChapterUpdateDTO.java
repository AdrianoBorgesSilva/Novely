package com.novely.novely.dto;

import java.io.Serializable;

import com.novely.novely.domain.Chapter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class ChapterUpdateDTO implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Size(max = 30, message = "Title must be at most 30 characters")
    private String title;

    @Size(max = 5000, message = "Content must be at most 5000 characters")
    private String content;

    @Min(value = 1, message = "Chapter number must be greater than 0")
    private Integer chapterNumber;

    public ChapterUpdateDTO() {

    }

    public ChapterUpdateDTO(Chapter obj) {
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

    public Integer getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(Integer chapterNumber) {
        this.chapterNumber = chapterNumber;
    }
}
