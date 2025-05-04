package com.novely.novely.service;

import org.springframework.stereotype.Service;

import com.novely.novely.domain.Novel;
import com.novely.novely.exception.UnauthorizedActionException;

@Service
public class DeleteNovelService {
    
    private final UserService userService;
    private final NovelService novelService;
    private final ChapterService chapterService;
    private final CommentService commentService;
    private final RatingService ratingService;

    public DeleteNovelService(UserService userService, NovelService novelService, ChapterService chapterService, CommentService commentService, RatingService ratingService){
        this.userService = userService;
        this.novelService = novelService;
        this.chapterService = chapterService;
        this.commentService = commentService;
        this.ratingService = ratingService;
    }

    public void deleteNovel(String novelId, String userId){
        
        Novel novel = novelService.findById(novelId);

        if (!novel.getAuthorId().equals(userId)) {
            throw new UnauthorizedActionException("You are not allowed to delete this novel.");
        }

        userService.removeNovelFromUser(userId, novelId);
        userService.removeCommentFromUser(userId, novelId);
        userService.removeRatingFromUser(userId, novelId);
        chapterService.deleteAllByNovelId(novelId);
        commentService.deleteAllByNovelId(novelId);
        ratingService.deleteAllByNovelId(novelId);
        
    
        novelService.deleteById(novelId);
    }
}
