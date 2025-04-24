package com.novely.novely.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.novely.novely.domain.Novel;
import com.novely.novely.enums.Genre;

public class NovelDTO implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private String id;

    private String authorId;

    private String title;
    private String description;
    private Genre genre;

    private LocalDate createdAt;

    private List<String> chaptersId;

    private List<String> ratingsId;

    private List<String> commentIds;

    private int views;

    private int numberOfFavorites;
    private int numberOfSuperFavorites;

    public NovelDTO() {

    }

    public NovelDTO(Novel obj) {
        this.id = obj.getId();
        this.authorId = obj.getAuthorId();
        this.title = obj.getTitle();
        this.description = obj.getDescription();
        this.genre = obj.getGenre();
        this.createdAt = obj.getCreatedAt();
        this.chaptersId = obj.getChaptersId();
        this.ratingsId = obj.getRatingsId();
        this.commentIds = obj.getCommentIds();
        this.views = obj.getViews();
        this.numberOfFavorites = obj.getNumberOfFavorites();
        this.numberOfSuperFavorites = obj.getNumberOfSuperFavorites();
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

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getChaptersId() {
        return chaptersId;
    }

    public void setChaptersId(List<String> chaptersId) {
        this.chaptersId = chaptersId;
    }

    public List<String> getRatingsId() {
        return ratingsId;
    }

    public void setRatingsId(List<String> ratingsId) {
        this.ratingsId = ratingsId;
    }

    public List<String> getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(List<String> commentIds) {
        this.commentIds = commentIds;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getNumberOfFavorites() {
        return numberOfFavorites;
    }

    public void setNumberOfFavorites(int numberOfFavorites) {
        this.numberOfFavorites = numberOfFavorites;
    }

    public int getNumberOfSuperFavorites() {
        return numberOfSuperFavorites;
    }

    public void setNumberOfSuperFavorites(int numberOfSuperFavorites) {
        this.numberOfSuperFavorites = numberOfSuperFavorites;
    }
}
