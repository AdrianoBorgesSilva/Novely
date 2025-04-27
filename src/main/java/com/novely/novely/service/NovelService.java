package com.novely.novely.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.novely.novely.domain.Chapter;
import com.novely.novely.domain.Comment;
import com.novely.novely.domain.Novel;
import com.novely.novely.domain.Rating;
import com.novely.novely.dto.ChapterCreateDTO;
import com.novely.novely.dto.CommentCreateDTO;
import com.novely.novely.dto.NovelCreateDTO;
import com.novely.novely.dto.NovelUpdateDTO;
import com.novely.novely.dto.RatingCreateDTO;
import com.novely.novely.exception.ObjectNotFoundException;
import com.novely.novely.exception.UnauthorizedActionException;
import com.novely.novely.repository.NovelRepository;

@Service
public class NovelService {
    
    @Autowired
    NovelRepository novelRepository;

    @Autowired
    UserService userService;

    @Lazy
    @Autowired
    ChapterService chapterService;

    @Lazy
    @Autowired
    CommentService commentService;

    @Lazy
    @Autowired
    RatingService ratingService;

    public List<Novel> findAll() {
        return novelRepository.findAll();
    }

    public Novel findById(String id) {
        return novelRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Novel not found"));
    }

    public List<Chapter> findChaptersFromNovel(String novelId) {
        return chapterService.findByNovelId(novelId);
    }

    public List<Comment> findCommentsFromNovel(String novelId) {
        return commentService.findByNovelId(novelId);
    }

    public List<Rating> findRatingsFromNovel(String novelId) {
        return ratingService.findByNovelId(novelId);
    }

    public Novel createNovel(NovelCreateDTO novelCreateDTO, String authorId){
        
        Novel novel = new Novel(null, authorId, novelCreateDTO.getTitle(), novelCreateDTO.getDescription(), novelCreateDTO.getGenre(), LocalDate.now(), 0, 0, 0);

        novel = novelRepository.save(novel);
        userService.addNovelToUser(authorId, novel.getId());

        return novel;
    } 

    public Chapter createChapter(ChapterCreateDTO chapterCreateDTO, String novelId, String userId) {
        return chapterService.createChapter(chapterCreateDTO, novelId, userId);
    }

    public Comment createComment(CommentCreateDTO commentCreateDTO, String authorId, String novelId){
        return commentService.createComment(commentCreateDTO, authorId, novelId);
    }

    public Rating createRating(RatingCreateDTO ratingCreateDTO, String novelId, String userId){
        return ratingService.createRating(ratingCreateDTO, novelId, userId);
    }

    public void updateNovel(NovelUpdateDTO novelUpdateDTO, String novelId, String userId) {
       
        Novel novel = novelRepository.findById(novelId).orElseThrow(() -> new ObjectNotFoundException("Novel not found"));

        if (!novel.getAuthorId().equals(userId)) {
            throw new UnauthorizedActionException("You can not update this novel");
        }
        
        if (novelUpdateDTO.getTitle() != null) {
            novel.setTitle(novelUpdateDTO.getTitle());
        }
        
        if (novelUpdateDTO.getDescription() != null) {
            novel.setDescription(novelUpdateDTO.getDescription());
        }
        
        if (novelUpdateDTO.getGenre() != null) {
            novel.setGenre(novelUpdateDTO.getGenre());
        }

        novelRepository.save(novel);
    }

    public void deleteNovel(String novelId, String userId){
        
        Novel novel = findById(novelId);

        if (!novel.getAuthorId().equals(userId)) {
            throw new UnauthorizedActionException("You are not allowed to delete this novel.");
        }

        userService.removeNovelFromUser(userId, novelId);
        userService.removeCommentFromUser(userId, novelId);
        userService.removeRatingFromUser(userId, novelId);
        chapterService.deleteAllByNovelId(novelId);
        commentService.deleteAllByNovelId(novelId);
        ratingService.deleteAllByNovelId(novelId);
        
    
        novelRepository.deleteById(novelId);
    }

    //Support Methods

    public Novel save(Novel novel) {
        return novelRepository.save(novel);
    }

    public Novel addChapterToNovel(String novelId, String chapterId) {
        Novel novel = findById(novelId);
        novel.getChaptersId().add(chapterId);
        return novelRepository.save(novel);
    }

    public Novel addCommentToNovel(String novelId, String commentId) {
        Novel novel = findById(novelId);
        novel.getCommentIds().add(commentId);
        return novelRepository.save(novel);
    }

    public Novel addRatingToNovel(String novelId, String ratingId) {
        Novel novel = findById(novelId);
        novel.getRatingsId().add(ratingId);
        return novelRepository.save(novel);
    }

    public void removeChapterFromNovel(String novelId, String chapterId) {
        Novel novel = findById(novelId);
        novel.getChaptersId().remove(chapterId); 
        novelRepository.save(novel);
    }

    public void removeCommentFromNovel(String novelId, String commentId) {
        Novel novel = findById(novelId);
        novel.getCommentIds().remove(commentId);
        novelRepository.save(novel);
    }

    public void removeRatingFromNovel(String novelId, String ratingId) {
        Novel novel = findById(novelId);
        novel.getRatingsId().remove(ratingId);
        novelRepository.save(novel);
    }

    public void incrementViews(String novelId) {
        Novel novel = findById(novelId);
        novel.setViews(novel.getViews() + 1);
        novelRepository.save(novel);
    }
}
