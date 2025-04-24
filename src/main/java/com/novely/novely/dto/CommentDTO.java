package com.novely.novely.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import com.novely.novely.domain.Comment;

public class CommentDTO implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private String id;

    private String authorId;
    private String novelId;
    private String content;
    
    private LocalDateTime commentedAt;
    
    private int likes;
    private int dislikes;

    private Set<String> likedList;
    private Set<String> dislikedList;

    public CommentDTO() {

    }

    public CommentDTO(Comment obj){
        this.id = obj.getId();
        this.authorId = obj.getAuthorId();
        this.novelId = obj.getNovelId();
        this.content = obj.getContent();
        this.commentedAt = obj.getCommentedAt();
        this.likes = obj.getLikes();
        this.dislikes = obj.getDislikes();
        this.likedList = obj.getLikedList();
        this.dislikedList = obj.getDislikedList();
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
}
