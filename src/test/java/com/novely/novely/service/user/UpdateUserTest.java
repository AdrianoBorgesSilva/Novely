package com.novely.novely.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.novely.novely.domain.User;
import com.novely.novely.dto.UserUpdateDTO;
import com.novely.novely.exception.ObjectNotFoundException;
import com.novely.novely.exception.UnauthorizedActionException;
import com.novely.novely.repository.UserRepository;
import com.novely.novely.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UpdateUserTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;
    
    @Test
    void testUpdateUser_UserNotFound() {
        
        // Arrange
        String userId = "notfound";
        UserUpdateDTO dto = new UserUpdateDTO();
        
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ObjectNotFoundException.class, () -> {userService.updateUser(dto, userId, userId);});
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateUser_UserNotAllowed() {

        // Arrange
        String userId = "100";
        String authId = "200";
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setName("New Test User");
        dto.setAvatarUrl("new-avatar.png");

        User user = new User(userId, "Test User", "test@gmail.com", "123456", 
        "default-avatar.png", LocalDate.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        UnauthorizedActionException exception = assertThrows(UnauthorizedActionException.class, ()->{userService.updateUser(dto, userId, authId);});

        //Assert
        assertEquals("You can not update this user", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
    
    @Test
    void testUpdateUser_UpdateNameWhenNotNull() {
        
        // Arrange
        String userId = "100";
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setName("New Test User");
        
        User user = new User(userId, "Test User", "test@gmail.com", "123456", "default-avatar.png", LocalDate.now());
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // Act
        userService.updateUser(dto, userId, userId);

        // Assert
        assertEquals("New Test User", user.getName());
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateUser_UpdateAvatarWhenNotNull() { 
        
        // Arrange
        String userId = "100";
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setAvatarUrl("new-avatar.png");
        
        User user = new User(userId, "Test User", "test@gmail.com", "123456", "default-avatar.png", LocalDate.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        userService.updateUser(dto, userId, userId);

        // Assert
        assertEquals("new-avatar.png", user.getAvatarUrl());
        verify(userRepository).save(user);
    
    }

    @Test
    void testUpdateUser_IgnoresNullFields() {
        
        // Arrange
        String userId = "100";
        UserUpdateDTO dto = new UserUpdateDTO();
        
        User user = new User(userId, "Test User", "test@gmail.com", "123456", "default-avatar.png", LocalDate.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        userService.updateUser(dto, userId, userId);

        // Assert
        assertEquals("Test User", user.getName());
        assertEquals("default-avatar.png", user.getAvatarUrl());
        verify(userRepository).save(user);
    }
}
