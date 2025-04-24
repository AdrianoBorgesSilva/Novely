package com.novely.novely.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novely.novely.domain.Chapter;
import com.novely.novely.domain.Novel;
import com.novely.novely.dto.ChapterCreateDTO;
import com.novely.novely.dto.ChapterUpdateDTO;
import com.novely.novely.exception.ObjectNotFoundException;
import com.novely.novely.exception.UnauthorizedActionException;
import com.novely.novely.repository.ChapterRepository;

@Service
public class ChapterService {
    
    @Autowired
    ChapterRepository chapterRepository;

    @Autowired
    UserService userService;

    @Autowired
    NovelService novelService;

    public List<Chapter> findAll() {
        return chapterRepository.findAll();
    }

    public Chapter findById(String id) {
        return chapterRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Chapter not found"));
    }

    public List<Chapter> findByNovelId(String novelId) {
        return chapterRepository.findByNovelId(novelId);
    }

    public Chapter createChapter(ChapterCreateDTO chapterCreateDTO, String novelId, String userId) {
        
        Novel novel = novelService.findById(novelId);
        
        if (!novel.getAuthorId().equals(userId)) {
            throw new UnauthorizedActionException("You can not add chapters in this novel");
        }

        Chapter chapter = new Chapter(null, novelId, chapterCreateDTO.getTitle(), chapterCreateDTO.getContent(), 1, LocalDateTime.now(), null);

        chapter = chapterRepository.save(chapter);
        novelService.addChapterToNovel(novelId, chapter.getId());

        return chapter;
    }

    public void updateChapter(ChapterUpdateDTO chapterUpdateDTO, String chapterId, String userId) {
        
        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new ObjectNotFoundException("Chapter not found"));
        Novel novel = novelService.findById(chapter.getNovelId());
    
        if (!novel.getAuthorId().equals(userId)) {
            throw new UnauthorizedActionException("You can not update this chapter");
        }
        
        if (chapterUpdateDTO.getTitle() != null) {
            chapter.setTitle(chapterUpdateDTO.getTitle());
        }
        
        if (chapterUpdateDTO.getContent() != null) {
            chapter.setContent(chapterUpdateDTO.getContent());
        }
        
        if (chapterUpdateDTO.getChapterNumber() != null) {
            chapter.setChapterNumber(chapterUpdateDTO.getChapterNumber());
        }
  
        chapterRepository.save(chapter);
    }

    public void deleteChapter(String chapterId, String userId) {
        
        Chapter chapter = findById(chapterId); 
        Novel novel = novelService.findById(chapter.getNovelId());
    
        if (!novel.getAuthorId().equals(userId)) {
            throw new UnauthorizedActionException("You can not delete this chapter");
        }
    
        novelService.removeChapterFromNovel(novel.getId(), chapterId);
        chapterRepository.deleteById(chapterId);
    }

    //Support Methods

    public void deleteAllByNovelId(String novelId) {
        List<Chapter> chapters = chapterRepository.findByNovelId(novelId);
        chapterRepository.deleteAll(chapters);
    }
} 

