package com.smrt.smartdiagnostics.Repositories;

import com.smrt.smartdiagnostics.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
