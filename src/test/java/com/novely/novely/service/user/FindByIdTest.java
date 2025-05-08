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
public class FindByIdTest {
    
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void testFindById_UserExistsAndNotDeleted() {

        // Arrange
        String userId = "100";
        User user = new User();
        user.setId(userId);
        user.setDeleted(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User result = userService.findById(userId);
        
        // Assert
        assertEquals(userId, result.getId());
        assertFalse(result.isDeleted());
        verify(userRepository).findById(userId);
    }

    @Test
    void testFindById_UserExistsAndDeleted() {
        
        // Arrange
        String userId = "100";
        User user = new User();
        user.setId(userId);
        user.setDeleted(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {userService.findById(userId);});
        
        // Assert
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(userId);
    }
    
    @Test
    void testFindById_userNotFound() {
        
        // Arrange
        when(userRepository.findById("notfound")).thenReturn(Optional.empty());

        // Act 
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {userService.findById("notfound");});
        
        // Assert
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById("notfound");
    }

   
}
