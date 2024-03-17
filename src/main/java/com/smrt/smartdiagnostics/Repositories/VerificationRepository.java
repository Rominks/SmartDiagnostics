package com.smrt.smartdiagnostics.Repositories;

import com.smrt.smartdiagnostics.Models.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {
    boolean existsByCode(String code);
    boolean existsByEmail(String email);
}
