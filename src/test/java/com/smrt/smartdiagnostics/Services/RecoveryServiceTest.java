package com.smrt.smartdiagnostics.Services;

import com.smrt.smartdiagnostics.Controllers.RecoveryController;
import com.smrt.smartdiagnostics.Controllers.RegisterController;
import com.smrt.smartdiagnostics.Models.Recovery;
import com.smrt.smartdiagnostics.Repositories.RecoveryRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecoveryServiceTest {
    private UserService userService;
    private RecoveryService recoveryService;
    private RecoveryRepository recoveryRepository;
    private JavaMailSender mailSender;
    private RegisterController registerController;
    RecoveryController recoveryController;
    @Before
    public void setUp() throws Exception {
        userService = mock(UserService.class);
        recoveryRepository = mock(RecoveryRepository.class);
        recoveryService = new RecoveryService(recoveryRepository, userService);
        mailSender = mock(JavaMailSender.class);
        recoveryController = new RecoveryController(userService, mailSender, recoveryService);
    }
    @Test
    public void saveVerification() {

        Recovery verification = new Recovery("test@test.com", "test", null);
        when(recoveryRepository.existsByEmail(verification.getEmail())).thenReturn(false);
        when(recoveryRepository.existsByCode(verification.getCode())).thenReturn(false);
        when(recoveryRepository.save(verification)).thenReturn(verification).thenReturn(null);
        assert recoveryService.saveVerification(verification).equals("Verification saved");

    }

    @Test
    public void saveVerificationEmailExist() {

        Recovery verification = new Recovery("test@test.com", "test", null);
        when(recoveryRepository.existsByEmail(verification.getEmail())).thenReturn(true);
        when(recoveryRepository.existsByCode(verification.getCode())).thenReturn(false);
        when(recoveryRepository.save(verification)).thenReturn(verification).thenReturn(null);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> recoveryService.saveVerification(verification));
        assert exception.getMessage().equals("Recovery for provided email already exists");

    }


    @Test
    public void saveVerificationCodeExist() {

        Recovery verification = new Recovery("test@test.com", "test", null);
        when(recoveryRepository.existsByEmail(verification.getEmail())).thenReturn(false);
        when(recoveryRepository.existsByCode(verification.getCode())).thenReturn(true);
        when(recoveryRepository.save(verification)).thenReturn(verification).thenReturn(null);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> recoveryService.saveVerification(verification));
        assert exception.getMessage().equals("Recovery code already exists");

    }

    @Test
    public void confirmVerification() {
        Recovery verification = new Recovery("test@test.com", "test", null);
        when(recoveryRepository.getVerificationByCode("test")).thenReturn(verification);
        assert recoveryService.confirmVerification("test").equals("deleted");
    }

    @Test
    public void getEmailByCode() {
        Recovery recovery = new Recovery("test@test.com", "test", null);
        when(recoveryRepository.getVerificationByCode("test")).thenReturn(recovery);
        assert recoveryService.getEmailByCode("test").equals("test@test.com");


    }
}