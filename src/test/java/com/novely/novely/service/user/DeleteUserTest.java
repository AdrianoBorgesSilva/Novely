package com.novely.novely.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import com.novely.novely.exception.UnauthorizedActionException;
import com.novely.novely.repository.UserRepository;
import com.novely.novely.service.UserService;

@ExtendWith(MockitoExtension.class)
public class DeleteUserTest {
    
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void testDeleteUser_UserNotAllowed() {
        
        // Arrange
        String userId = "100";
        String authId = "200";
        
        User user = new User(userId, "Test User", "test@gmail.com", "123456", "default-avatar.png", LocalDate.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        UnauthorizedActionException exception = assertThrows(UnauthorizedActionException.class, () -> {
            userService.deleteUser(userId, authId);
        });
        
        // Assert
        assertEquals("You can not delete this account", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testDeleteUser_UserAllowed() {
       
        // Arrange
        String userId = "100";
        String authId = "100";
        
        User user = new User(authId, "Test User", "test@gmail.com", "123456", "default-avatar.png", LocalDate.now());
        
        // Simular retorno dos métodos
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        userService.deleteUser(userId, authId);

        // Assert
        assertEquals("User deleted", user.getName());
        assertTrue(user.getEmail().startsWith("anon_"));
        assertEquals("", user.getPassword());
        assertEquals("deleted_user.png", user.getAvatarUrl());
        assertTrue(user.isDeleted());
        verify(userRepository).save(user);
    }
}
