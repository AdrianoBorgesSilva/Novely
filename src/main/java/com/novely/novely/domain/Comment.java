package com.novely.novely.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comment")
public class Comment implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    private String authorId;
    private String novelId;
    private String content;
    
    private LocalDateTime commentedAt;
    
    private int likes;
    private int dislikes;

    private Set<String> likedList = new HashSet<>();
    private Set<String> dislikedList = new HashSet<>();

    public Comment() {
        
    }
    
    public Comment(String id, String authorId, String novelId, String content, LocalDateTime commentedAt) {
        this.id = id;
        this.authorId = authorId;
        this.novelId = novelId;
        this.content = content;
        this.commentedAt = commentedAt;
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

    public String getNovelId() {
        return novelId;
    }

    public void setNovelId(String novelId) {
        this.novelId = novelId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(LocalDateTime commentedAt) {
        this.commentedAt = commentedAt;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public Set<String> getLikedList() {
        return likedList;
    }

    public void setLikedList(Set<String> likedList) {
        this.likedList = likedList;
    }

    public Set<String> getDislikedList() {
        return dislikedList;
    }

    public void setDislikedList(Set<String> dislikedList) {
        this.dislikedList = dislikedList;
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
        Comment other = (Comment) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
