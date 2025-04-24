package com.novely.novely.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.novely.novely.domain.Chapter;

public class ChapterDTO implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private String id;

    private String novelId;

    private String title;

    private String content;
    
    private int chapterNumber;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ChapterDTO() {

    }

    public ChapterDTO(Chapter obj) {
        this.id = obj.getId();
        this.novelId = obj.getNovelId();
        this.title = obj.getTitle();
        this.content = obj.getContent();
        this.chapterNumber = obj.getChapterNumber();
        this.createdAt = obj.getCreatedAt();
        this.updatedAt = obj.getUpdatedAt();
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

    public String getNovelId() {
        return novelId;
    }

    public void setNovelId(String novelId) {
        this.novelId = novelId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
