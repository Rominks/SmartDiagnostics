package com.***REMOVED***.smartdiagnostics.Services;

import com.***REMOVED***.smartdiagnostics.Repositories.UserRepository;
import com.***REMOVED***.smartdiagnostics.Users.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
