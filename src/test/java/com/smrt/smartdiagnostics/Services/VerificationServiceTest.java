package com.smrt.smartdiagnostics.Services;

import com.smrt.smartdiagnostics.Models.Verification;
import com.smrt.smartdiagnostics.Repositories.VerificationRepository;
import com.smrt.smartdiagnostics.Services.VerificationService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VerificationServiceTest {
    private VerificationRepository verificationRepository;
    private VerificationService verificationService;

    @Before
    public void setup() {
        verificationRepository = mock(VerificationRepository.class);
        verificationService = new VerificationService(verificationRepository);
    }

    @Test
    public void testSaveVerification_NewVerification() {
        // Given
        Verification verification = new Verification();
        verification.setEmail("test@example.com");
        verification.setCode("123456");

        // When
        when(verificationRepository.existsByEmail(verification.getEmail())).thenReturn(false);
        when(verificationRepository.existsByCode(verification.getCode())).thenReturn(false);

        verificationService.saveVerification(verification);

        // Then
        verify(verificationRepository).existsByEmail(verification.getEmail());
        verify(verificationRepository).existsByCode(verification.getCode());
        verify(verificationRepository).save(verification);
    }

    @Test
    public void testSaveVerification_ExistingEmail() {
        // Given
        Verification verification = new Verification();
        verification.setEmail("test@example.com");
        verification.setCode("123456");

        // When
        when(verificationRepository.existsByEmail(verification.getEmail())).thenReturn(true);

        // Then
        assertThrows(IllegalStateException.class, () -> verificationService.saveVerification(verification));

        verify(verificationRepository, never()).existsByCode(anyString());
        verify(verificationRepository, never()).save(any(Verification.class));
    }

    @Test
    public void testSaveVerification_ExistingCode() {
        // Given
        Verification verification = new Verification();
        verification.setEmail("new@example.com");
        verification.setCode("123456");

        // When
        when(verificationRepository.existsByEmail(verification.getEmail())).thenReturn(false);
        when(verificationRepository.existsByCode(verification.getCode())).thenReturn(true);

        // Then
        assertThrows(IllegalStateException.class, () -> verificationService.saveVerification(verification));

        verify(verificationRepository).existsByEmail(verification.getEmail());
        verify(verificationRepository).existsByCode(verification.getCode());
        verify(verificationRepository, never()).save(any(Verification.class));
    }

    @Test
    public void testConfirmVerification() {
        // Given
        String code = "verificationCode";
        Verification verification = new Verification();
        VerificationRepository verificationRepositoryMock = mock(VerificationRepository.class);
        when(verificationRepositoryMock.getVerificationByCode(code)).thenReturn(verification);
        VerificationService verificationService = new VerificationService(verificationRepositoryMock);

        // When
        verificationService.confirmVerification(code);

        // Then
        verify(verificationRepositoryMock).delete(verification);
    }

    @Test
    public void testGetEmailByCode() {
        // Given
        String code = "verificationCode";
        String email = "test@example.com";
        Verification verification = new Verification();
        verification.setEmail(email);
        VerificationRepository verificationRepositoryMock = mock(VerificationRepository.class);
        when(verificationRepositoryMock.getVerificationByCode(code)).thenReturn(verification);
        VerificationService verificationService = new VerificationService(verificationRepositoryMock);

        // When
        String result = verificationService.getEmailByCode(code);

        // Then
        assertEquals(email, result);
    }
}
