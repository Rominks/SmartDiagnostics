package com.***REMOVED***.smartdiagnostics.Repositories;

import com.***REMOVED***.smartdiagnostics.Models.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {
    boolean existsByCode(String code);
    boolean existsByEmail(String email);

    Verification getVerificationByCode(String code);
}
