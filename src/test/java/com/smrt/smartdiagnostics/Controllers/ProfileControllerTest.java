package com.smrt.smartdiagnostics.Controllers;

import com.smrt.smartdiagnostics.Controllers.ProfileController;
import com.smrt.smartdiagnostics.Models.User;
import com.smrt.smartdiagnostics.Services.UserService;
import org.hibernate.exception.JDBCConnectionException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProfileControllerTest {
    private UserService userService;
    private ProfileController profileController;

    @Before
    public void setup() {
        userService = mock(UserService.class);
        profileController = new ProfileController(userService);
    }

    @Test
    public void testDeleteUser_Success() {
        // Given
        long userId = 1L;

        // When
        profileController.deleteUser(userId);

        // Then
        verify(userService).deleteUser(userId);
    }

    @Test
    public void testUpdateUser_Success() {
        // Given
        long userId = 1L;
        User newInfo = new User();
        newInfo.setEmail("new@example.com");
        newInfo.setPassword("newpassword");
        newInfo.setUsername("newusername");

        // When
        ResponseEntity response = profileController.updateUser(userId, newInfo);

        // Then
        verify(userService).updateCredentials(userId, newInfo);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateUser_NotFound() {
        // Given
        long userId = 1L;
        User newInfo = new User();
        newInfo.setEmail("new@example.com");
        newInfo.setPassword("newpassword");
        newInfo.setUsername("newusername");

        doThrow(new IllegalStateException()).when(userService).updateCredentials(userId, newInfo);

        // When
        ResponseEntity response = profileController.updateUser(userId, newInfo);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateUser_InternalServerError() {
        // Given
        long userId = 1L;
        User newInfo = new User();
        newInfo.setEmail("new@example.com");
        newInfo.setPassword("newpassword");
        newInfo.setUsername("newusername");

        doThrow(new JDBCConnectionException("Error message", new SQLException())).when(userService).updateCredentials(userId, newInfo);

        // When
        ResponseEntity response = profileController.updateUser(userId, newInfo);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
