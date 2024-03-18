package com.smrt.smartdiagnostics.Services;

import com.smrt.smartdiagnostics.Models.Recovery;
import com.smrt.smartdiagnostics.Models.User;
import com.smrt.smartdiagnostics.Repositories.RecoveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Optional;

@Service
public class RecoveryService {
    private final RecoveryRepository recoveryRepository;

    private final UserService userService;
    @Autowired
    public RecoveryService(RecoveryRepository recoveryRepository, UserService userService) {
        this.recoveryRepository = recoveryRepository;

        this.userService = userService;
    }

    public void saveVerification(Recovery verification) {
        if (!recoveryRepository.existsByEmail(verification.getEmail())) {
            if (!recoveryRepository.existsByCode(verification.getCode())) {
                recoveryRepository.save(verification);
            } else {
                throw new IllegalStateException("Recovery code already exists");
            }
        } else {
            throw  new IllegalStateException("Recovery for provided email already exists");
        }
    }

    public void confirmVerification(String code) {
        Recovery recovery = recoveryRepository.getVerificationByCode(code);
        recoveryRepository.delete(recovery);
    }

    public String getEmailByCode(String code) {
        Recovery recovery = recoveryRepository.getVerificationByCode(code);
        return recovery.getEmail();
    }
}
