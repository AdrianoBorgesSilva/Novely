package com.novely.novely.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.novely.novely.domain.Rating;

public class RatingDTO implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private String id;

    private int rate;

    private String authorId;
    private String novelId;

    private LocalDateTime ratedAt;

    public RatingDTO() {

    }

    public RatingDTO(Rating obj) {
        this.id = obj.getId();
        this.rate = obj.getRate();
        this.authorId = obj.getAuthorId();
        this.novelId = obj.getNovelId();
        this.ratedAt = obj.getRatedAt();
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

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getNovelId() {
        return novelId;
    }

    public void setNovelId(String novelId) {
        this.novelId = novelId;
    }

    public LocalDateTime getRatedAt() {
        return ratedAt;
    }

    public void setRatedAt(LocalDateTime ratedAt) {
        this.ratedAt = ratedAt;
    }
}
