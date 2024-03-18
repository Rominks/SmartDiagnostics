package com.smrt.smartdiagnostics.Services;

import com.smrt.smartdiagnostics.Repositories.UserRepository;
import com.smrt.smartdiagnostics.Models.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void createUser(User user) {
        // Grožis
        if (!userRepository.existsUserByEmail(user.getEmail())) {
            if (!userRepository.existsUserByPassword(user.getPassword())) {
                userRepository.save(user);
            } else {
                throw new IllegalStateException("An account exists with the provided password. :)");
            }
        } else {
            throw new IllegalStateException("An account exists with the provided email.");
        }
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }
    public Optional<User> getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    public void updateUser(User user) {
        userRepository.saveAndFlush(user);
    }
}
