package com.smrt.smartdiagnostics.Repositories;

import com.smrt.smartdiagnostics.Models.Recovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecoveryRepository extends JpaRepository<Recovery, Long> {
    boolean existsByCode(String code);
    boolean existsByEmail(String email);

    Recovery getVerificationByCode(String code);
}
