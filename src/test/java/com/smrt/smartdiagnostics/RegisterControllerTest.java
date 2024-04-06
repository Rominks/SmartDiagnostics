package com.smrt.smartdiagnostics;

import com.smrt.smartdiagnostics.Controllers.RegisterController;
import com.smrt.smartdiagnostics.Models.User;
import com.smrt.smartdiagnostics.Models.Verification;
import com.smrt.smartdiagnostics.Services.UserService;
import com.smrt.smartdiagnostics.Services.VerificationService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RegisterControllerTest {
    private UserService userService;
    private VerificationService verificationService;
    private JavaMailSender mailSender;
    private RegisterController registerController;

    @Before
    public void setup() {
        userService = mock(UserService.class);
        verificationService = mock(VerificationService.class);
        mailSender = mock(JavaMailSender.class);

        registerController = new RegisterController(userService, verificationService, mailSender);
    }

    @Test
    public void testSubmitUser_Success() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("test");
        user.setUsername("username");

        // When
        doNothing().when(userService).createUser(user);
        doNothing().when(mailSender).send(new SimpleMailMessage());
        ResponseEntity response = registerController.submitUser(user);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Account registered. A verification will be sent to the provided email.", response.getBody());
        verify(userService).createUser(user);
    }

    @Test
    public void testSubmitUser_NotAcceptable() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("test");
        user.setUsername("username");
        doThrow(new IllegalStateException()).when(userService).createUser(user);

        // When
        doNothing().when(mailSender).send(new SimpleMailMessage());
        ResponseEntity response = registerController.submitUser(user);

        // Then
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals("Account registration failed. Either the email or password is in use.", response.getBody());
    }

    @Test
    public void testVerifyUser_UserVerified() {
        // Given
        String code = "123456";
        String email = "test@example.com";

        when(verificationService.getEmailByCode(code)).thenReturn(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(new User()));

        // When
        ResponseEntity<String> response = registerController.verifyUser(code);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(verificationService).confirmVerification(code);
        verify(userService).updateUser(any());
    }

    @Test
    public void testVerifyUser_CodeNotFound() {
        // Given
        String code = "123456";

        when(verificationService.getEmailByCode(code)).thenReturn(null);

        // When
        ResponseEntity<String> response = registerController.verifyUser(code);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testVerifyUser_UserNotFound() {
        // Given
        String code = "123456";
        String email = "test@example.com";

        when(verificationService.getEmailByCode(code)).thenReturn(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.empty());

        // When
        ResponseEntity<String> response = registerController.verifyUser(code);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, never()).updateUser(any());
    }
}
