package com.smrt.smartdiagnostics.Services;

import com.smrt.smartdiagnostics.Models.User;
import com.smrt.smartdiagnostics.Models.Verification;
import com.smrt.smartdiagnostics.Repositories.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationService {
    private final VerificationRepository verificationRepository;
    @Autowired
    public VerificationService(VerificationRepository verificationRepository) {
        this.verificationRepository = verificationRepository;
    }

    public void saveVerification(Verification verification) {
        if (!verificationRepository.existsByEmail(verification.getEmail())) {
            if (!verificationRepository.existsByCode(verification.getCode())) {
                verificationRepository.save(verification);
            } else {
                throw new IllegalStateException("Verification code already exists");
            }
        } else {
            throw  new IllegalStateException("Verification for provided email already exists");
        }
    }

    public void confirmVerification(String code) {
        Verification verification = verificationRepository.getVerificationByCode(code);
        verificationRepository.delete(verification);
    }

    public String getEmailByCode(String code) {
        Verification verification = verificationRepository.getVerificationByCode(code);
        if(verification == null) {
            return null;
        }
        return verification.getEmail();
    }
}
