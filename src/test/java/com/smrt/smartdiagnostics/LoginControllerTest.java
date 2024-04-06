package com.smrt.smartdiagnostics;

import com.smrt.smartdiagnostics.Controllers.LoginController;
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
public class LoginControllerTest {
    private UserService userService;
    private LoginController loginController;

    @Before
    public void setup() {
        userService = mock(UserService.class);
        loginController = new LoginController(userService);
    }

    @Test
    public void testConfirmLogin_Success() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("test");
        user.setUsername("username");

        // When
        when(userService.getUserByCredentials(user)).thenReturn(user);
        ResponseEntity response = loginController.submitLogin(user);

        // Then
        verify(userService).getUserByCredentials(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testConfirmLogin_NotAcceptable() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("test");
        user.setUsername("username");
        doThrow(new IllegalStateException()).when(userService).getUserByCredentials(user);

        // When
        ResponseEntity response = loginController.submitLogin(user);

        // Then
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }

    @Test
    public void testConfirmLogin_InternalServerError() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("test");
        user.setUsername("username");
        doThrow(new JDBCConnectionException("Error message", new SQLException())).when(userService).getUserByCredentials(user);

        // When
        ResponseEntity response = loginController.submitLogin(user);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
