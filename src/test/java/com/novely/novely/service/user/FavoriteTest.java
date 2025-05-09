package com.novely.novely.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.novely.novely.domain.Novel;
import com.novely.novely.domain.User;
import com.novely.novely.exception.FavoritesException;
import com.novely.novely.exception.UnauthorizedActionException;
import com.novely.novely.repository.UserRepository;
import com.novely.novely.service.NovelService;
import com.novely.novely.service.UserService;

@ExtendWith(MockitoExtension.class)
public class FavoriteTest {

    @Mock
    UserRepository userRepository;

    @Mock
    NovelService novelService;

    @InjectMocks
    UserService userService;

    @Test
    void testAddFavorite_UserNotAllowed() {
        
        //Arrange
        String userId = "100";
        String authId = "200";
        String novelId = "500";

        User user = new User();
        user.setId(userId);
        user.setFavorites(new HashSet<>());
        user.setSuperFavorites(new HashSet<>());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        UnauthorizedActionException exception = assertThrows(UnauthorizedActionException.class, () -> {userService.addFavorite(userId, novelId, authId);});

        // Assert
        assertEquals("You can not add favorites in this user", exception.getMessage());
        verify(userRepository, never()).save(any());
        verify(novelService, never()).save(any());
    }
    
    @Test
    void testAddFavorite_UserAllowed() {
        
        //Arrange
        String userId = "100";
        String authId = "100";
        String novelId = "500";

        User user = new User();
        user.setId(userId);
        user.setFavorites(new HashSet<>());
        user.setSuperFavorites(new HashSet<>());

        Novel novel = new Novel();
        novel.setId(novelId);
        novel.setNumberOfFavorites(0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(novelService.findById(novelId)).thenReturn(novel);

        // Act
        userService.addFavorite(userId, novelId, authId);

        // Assert
        assertTrue(user.getFavorites().contains(novelId));
        assertEquals(1, novel.getNumberOfFavorites());
        verify(userRepository).save(user);
        verify(novelService).save(novel);
    }

    @Test
    void testAddFavorite_AlreadyFavorite() {
        
        //Arrange
        String userId = "100";
        String authId = "100";
        String novelId = "500";

        User user = new User();
        user.setId(userId);
        user.setFavorites(new HashSet<>(Set.of(novelId)));
        user.setSuperFavorites(new HashSet<>());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        FavoritesException exception = assertThrows(FavoritesException.class, () -> {userService.addFavorite(userId, novelId, authId);});

        //Assert
        assertEquals("Novel already in favorites", exception.getMessage());
        verify(userRepository, never()).save(any());
        verify(novelService, never()).save(any());
    }

    @Test
    void testRemoveFavorite_shouldRemove() {
        
        //Arrange
        String userId = "100";
        String authId = "100";
        String novelId = "500";

        User user = new User();
        user.setId(userId);
        user.setFavorites(new HashSet<>(Set.of(novelId)));

        Novel novel = new Novel();
        novel.setId(novelId);
        novel.setNumberOfFavorites(5);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(novelService.findById(novelId)).thenReturn(novel);

        // Act
        userService.removeFavorite(userId, novelId, authId);

        // Assert
        assertFalse(user.getFavorites().contains(novelId));
        assertEquals(4, novel.getNumberOfFavorites());
        verify(userRepository).save(user);
        verify(novelService).save(novel);
    }
}
