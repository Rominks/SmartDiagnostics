package com.smrt.smartdiagnostics.Repositories;

import com.smrt.smartdiagnostics.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);
    boolean existsUserByPassword(String pswd);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByUsername(String username);
}
