package com.novely.novely.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.novely.novely.domain.User;
import com.novely.novely.exception.ObjectNotFoundException;
import com.novely.novely.repository.UserRepository;
import com.novely.novely.service.UserService;

@ExtendWith(MockitoExtension.class)
public class FindByEmail {
    
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void testFindByEmail_UserExistsAndNotDeleted() {

        // Arrange
        String userEmail = "test@gmail.com";
        User user = new User();
        user.setEmail(userEmail);
        user.setDeleted(false);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        // Act
        User result = userService.findByEmail(userEmail);
        
        // Assert
        assertEquals(userEmail, result.getEmail());
        assertFalse(result.isDeleted());
        verify(userRepository).findByEmail(userEmail);
    }

    @Test
    void testFindByEmail_UserExistsAndDeleted() {
        
        // Arrange
        String userEmail = "test@gmail.com";
        User user = new User();
        user.setEmail(userEmail);
        user.setDeleted(true);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        // Act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {userService.findByEmail(userEmail);});

        // Assert
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findByEmail(userEmail);
    }

    @Test
    void testFindByEmail_userNotFound() {
        
        // Arrange
        when(userRepository.findByEmail("notfound@gmail.com")).thenReturn(Optional.empty());

        // Act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {userService.findByEmail("notfound@gmail.com");});

        // Assert
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findByEmail("notfound@gmail.com");
    }

    
}
