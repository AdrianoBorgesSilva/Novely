package com.novely.novely.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.novely.novely.domain.User;
import com.novely.novely.repository.UserRepository;
import com.novely.novely.service.UserService;

@ExtendWith(MockitoExtension.class)
public class FindAllTest {
    
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void testFindAll_ReturnAllNoFilter() {

        // Arrange
        User activeUser = new User();
        activeUser.setId("100");
        activeUser.setDeleted(false);

        User deletedUser = new User();
        deletedUser.setId("110");
        deletedUser.setDeleted(true);

        when(userRepository.findAll()).thenReturn(List.of(activeUser, deletedUser));

        // Act
        List<User> users = userService.findAll();

        // Assert
        assertEquals(2, users.size());
        verify(userRepository).findAll();
    }
}
