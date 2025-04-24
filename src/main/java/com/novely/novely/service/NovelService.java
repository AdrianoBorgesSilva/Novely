package com.novely.novely.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.novely.novely.domain.Novel;
import com.novely.novely.dto.NovelCreateDTO;
import com.novely.novely.dto.NovelUpdateDTO;
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

    public List<Novel> findAll() {
        return novelRepository.findAll();
    }

    public Novel findById(String id) {
        return novelRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Novel not found"));
    }

    public Novel createNovel(NovelCreateDTO novelCreateDTO, String authorId){
        
        Novel novel = new Novel(null, authorId, novelCreateDTO.getTitle(), novelCreateDTO.getDescription(), novelCreateDTO.getGenre(), LocalDate.now(), 0, 0, 0);

        novel = novelRepository.save(novel);
        userService.addNovelToUser(authorId, novel.getId());

        return novel;
    } 

    public void updateNovel(NovelUpdateDTO novelUpdateDTO, String novelId, String userId) {
       
        Novel novel = novelRepository.findById(novelId).orElseThrow(() -> new ObjectNotFoundException("Novel not found"));

        if (!novel.getAuthorId().equals(userId)) {
            throw new UnauthorizedActionException("You can not update this novel");
        }
        
        if (novelUpdateDTO.getTitle() != null) {
            novel.setTitle(novelUpdateDTO.getTitle());
        
        }else if (novelUpdateDTO.getDescription() != null) {
            novel.setDescription(novelUpdateDTO.getDescription());
        
        }else if (novelUpdateDTO.getGenre() != null) {
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
        chapterService.deleteAllByNovelId(novelId);
    
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

    public void addView(String novelId) {
        Novel novel = findById(novelId);
        novel.setViews(novel.getViews() + 1);
        novelRepository.save(novel);
    }
}
