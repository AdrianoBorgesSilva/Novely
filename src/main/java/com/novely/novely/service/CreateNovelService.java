package com.novely.novely.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.novely.novely.domain.Novel;
import com.novely.novely.dto.NovelCreateDTO;

@Service
public class CreateNovelService {
    
    private final NovelService novelService;
    private final UserService userService;

    public CreateNovelService(NovelService novelService, UserService userService) {
        this.novelService = novelService;
        this.userService = userService;
    }

    public Novel createNovel(NovelCreateDTO novelCreateDTO, String authorId){
        
        Novel novel = new Novel(null, authorId, novelCreateDTO.getTitle(), novelCreateDTO.getDescription(), novelCreateDTO.getGenre(), LocalDate.now(), 0, 0, 0);

        novel = novelService.save(novel);
        userService.addNovelToUser(authorId, novel.getId());

        return novel;
    } 
}
