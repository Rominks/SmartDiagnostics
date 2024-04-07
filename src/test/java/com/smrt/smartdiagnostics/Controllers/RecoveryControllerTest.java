package com.smrt.smartdiagnostics.Controllers;

import com.smrt.smartdiagnostics.Models.User;
import com.smrt.smartdiagnostics.Services.RecoveryService;
import com.smrt.smartdiagnostics.Services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecoveryControllerTest {
    private UserService userService;
    private RecoveryService recoveryService;
    private JavaMailSender mailSender;
    private RegisterController registerController;
    RecoveryController recoveryController;
    @Before
    public void setUp() throws Exception {
        userService = mock(UserService.class);
        recoveryService = mock(RecoveryService.class);
        mailSender = mock(JavaMailSender.class);
        recoveryController = new RecoveryController(userService, mailSender, recoveryService);
    }

    @Test
    public void resetPassword() {
        //create new user
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setUsername("test123");
        when(userService.getUserByEmail("test@test.com")).thenReturn(Optional.of(user));
        assert (recoveryController.resetPassword("test@test.com").getStatusCode() == HttpStatus.OK);

    }
    @Test
    public void resetPasswordFailed() {
        User user = new User();
        when(userService.getUserByEmail("test")).thenReturn(Optional.of(user));
        when(userService.getUserByUsername("test")).thenReturn(Optional.of(user));
        assert (recoveryController.resetPassword("testas").getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void generatePasswordCode() {
        assert RecoveryController.generatePasswordCode().length() == 25;
    }

    @Test
    public void testResetPassword() {
        String code = "test";
        when(recoveryService.getEmailByCode(code)).thenReturn("test@test.com");
        when(userService.getUserByEmail("test@test.com")).thenReturn(Optional.of(new User()));
        assert (recoveryController.resetPassword(code,"newPass").getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void getUsernameByCode() {
        String code = "test";
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setUsername("test123");
        when(recoveryService.getEmailByCode(code)).thenReturn("test@test.com");
        when(userService.getUserByEmail("test@test.com")).thenReturn(Optional.of(user));
        assert (recoveryController.getUsernameByCode(code) == "test@test.com");
    }
}