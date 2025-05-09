package com.novely.novely.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.novely.novely.domain.User;
import com.novely.novely.dto.UserSignUpDTO;
import com.novely.novely.exception.SignUpException;
import com.novely.novely.repository.UserRepository;
import com.novely.novely.service.UserService;

@ExtendWith(MockitoExtension.class)
public class CreateUserTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;
    
    @Test
    void testCreateUser_EmailAlreadyExists(){

        // Arrange
        String userName = "Test";
        String userEmail = "test@gmail.com";
        String userPassword = "123456";

        UserSignUpDTO dto = new UserSignUpDTO();
        dto.setName(userName);
        dto.setEmail(userEmail);
        dto.setPassword(userPassword);

        when(userRepository.existsByEmail(userEmail)).thenReturn(true);

        // Act & Assert
        assertThrows(SignUpException.class, () -> {userService.createUser(dto);});
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateUser_SetDefaultValues() {
        
        // Arrange
        String userName = "Test";
        String userEmail = "test@gmail.com";
        String userPassword = "123456";

        UserSignUpDTO dto = new UserSignUpDTO();
        dto.setName(userName);
        dto.setEmail(userEmail);
        dto.setPassword(userPassword);
        
        when(userRepository.existsByEmail(userEmail)).thenReturn(false);
        when(passwordEncoder.encode(userPassword)).thenReturn("$2a$10$hashedpassword");
        
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        
        when(userRepository.save(captor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.createUser(dto);

        // Assert
        User userToSave = captor.getValue();
        assertEquals(userName, userToSave.getName());
        assertEquals(userEmail, userToSave.getEmail());
        assertEquals("$2a$10$hashedpassword", userToSave.getPassword());
        assertEquals("default-avatar.png", userToSave.getAvatarUrl());
        assertNotNull(userToSave.getCreatedAt());

        // Assert
        assertSame(userToSave, result);
        assertEquals("$2a$10$hashedpassword", result.getPassword());
        verify(userRepository).existsByEmail(userEmail);
        verify(passwordEncoder).encode(userPassword);
        verify(userRepository).save(any(User.class));
    }
}
