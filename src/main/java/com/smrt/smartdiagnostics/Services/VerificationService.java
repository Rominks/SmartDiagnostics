package com.***REMOVED***.smartdiagnostics.Services;

import com.***REMOVED***.smartdiagnostics.Models.User;
import com.***REMOVED***.smartdiagnostics.Models.Verification;
import com.***REMOVED***.smartdiagnostics.Repositories.VerificationRepository;
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
        return verification.getEmail();
    }
}
