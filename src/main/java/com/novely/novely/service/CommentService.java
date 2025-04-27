package com.novely.novely.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novely.novely.domain.Comment;
import com.novely.novely.domain.Novel;
import com.novely.novely.domain.User;
import com.novely.novely.dto.CommentCreateDTO;
import com.novely.novely.exception.ObjectNotFoundException;
import com.novely.novely.exception.UnauthorizedActionException;
import com.novely.novely.repository.CommentRepository;

@Service
public class CommentService {
    
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserService userService;

    @Autowired
    NovelService novelService;

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public Comment findById(String commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new ObjectNotFoundException("Comment not found."));
    }

    public List<Comment> findByNovelId(String novelId){
        return commentRepository.findByNovelId(novelId);
    }

    public Comment createComment(CommentCreateDTO commentCreateDTO, String authorId, String novelId) {

        User user = userService.findById(authorId);
        Novel novel = novelService.findById(novelId);

        Comment comment = new Comment(null, authorId, novelId, commentCreateDTO.getContent(), LocalDateTime.now());
       
        comment = commentRepository.save(comment);
        userService.addCommentToUser(user.getId(), comment.getId());
        novelService.addCommentToNovel(novel.getId(), comment.getId());

        return comment;
    }

    public void deleteComment(String commentId, String userId) {

        Comment comment = findById(commentId);

        if (!comment.getAuthorId().equals(userId)) {
            throw new UnauthorizedActionException("You can not delete this comment");
        }

        userService.removeCommentFromUser(userId, commentId);
        novelService.removeCommentFromNovel(comment.getNovelId(), commentId);
        commentRepository.deleteById(commentId);
    }

    //Secondary Methods

    public Comment likeComment(String commentId, String userId) {
        
        Comment comment = findById(commentId);
    
        if (comment.getLikedList().contains(userId)) {
            throw new IllegalStateException("You have already liked this comment.");
        }
    
        comment.getDislikedList().remove(userId);
        comment.getLikedList().add(userId);
    
        comment.setLikes(comment.getLikedList().size());
        comment.setDislikes(comment.getDislikedList().size());
    
        return commentRepository.save(comment);
    }

    public Comment dislikeComment(String commentId, String userId) {
        
        Comment comment = findById(commentId);
    
        if (comment.getDislikedList().contains(userId)) {
            throw new IllegalStateException("You have already disliked this comment.");
        }
    
        comment.getLikedList().remove(userId);
        comment.getDislikedList().add(userId);
    
        comment.setLikes(comment.getLikedList().size());
        comment.setDislikes(comment.getDislikedList().size());
    
        return commentRepository.save(comment);
    }

    public void deleteAllByNovelId(String novelId) {
        List<Comment> list = commentRepository.findByNovelId(novelId);
        commentRepository.deleteAll(list);
    }
}
