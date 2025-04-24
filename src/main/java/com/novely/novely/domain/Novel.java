package com.novely.novely.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.novely.novely.enums.Genre;

@Document(collection = "novel")
public class Novel implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String authorId;
    private String title;
    private String description;
    private Genre genre;

    private LocalDate createdAt;

    private List<String> chaptersId = new ArrayList<>();

    private List<String> ratingsId = new ArrayList<>();

    private List<String> commentIds = new ArrayList<>();

    private int views;

    private int numberOfFavorites;
    private int numberOfSuperFavorites;

    public Novel() {

    }

    public Novel(String id, String authorId, String title, String description, Genre genre, LocalDate createdAt, int views, int numberOfFavorites, int numberOfSuperFavorites) {
        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.createdAt = createdAt;
        this.views = views;
        this.numberOfFavorites = numberOfFavorites;
        this.numberOfSuperFavorites = numberOfSuperFavorites;
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
        Novel other = (Novel) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
