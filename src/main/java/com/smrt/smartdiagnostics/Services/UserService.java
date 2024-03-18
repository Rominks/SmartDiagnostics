package com.***REMOVED***.smartdiagnostics.Services;

import com.***REMOVED***.smartdiagnostics.Helpers.ValidationHelper;
import com.***REMOVED***.smartdiagnostics.Repositories.UserRepository;
import com.***REMOVED***.smartdiagnostics.Models.User;
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

    @Transactional
    public void updateUser(User user) {
        userRepository.saveAndFlush(user);
    }
    public User getUserByCredentials(User user) {
        Optional<User> result;
        if (user.getUsername() != null) {
            result = userRepository.getUserByUsernameAndPassword(user.getUsername(), user.getPassword());
            return result.isPresent() ? result.get() : null;
        } else {
            result = userRepository.getUserByEmailAndPassword(user.getEmail(), user.getPassword());
            return result.isPresent() ? result.get() : null;
        }
    }

    @Transactional
    public void deleteUser(long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
        }
    }

    @Transactional
    public void updateCredentials(long userId, User newInfo) {
        // regex :)
        String pattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new IllegalStateException("No such user found."));

        if (newInfo.getEmail() != null && !newInfo.getEmail().isEmpty()) {
            if (ValidationHelper.patternMatches(newInfo.getEmail(), pattern)) {
                user.setEmail(newInfo.getEmail().trim());
            }
        }
        if (newInfo.getPassword() != null && !newInfo.getPassword().isEmpty()) {
            user.setPassword(newInfo.getPassword().trim());
        }
        if (newInfo.getUsername() != null && !newInfo.getUsername().isEmpty()) {
            user.setUsername(newInfo.getUsername().trim());
        }
        if (newInfo.getName() != null && !newInfo.getName().isEmpty()) {
            user.setName(newInfo.getName().trim());
        }
        if (newInfo.getSurname() != null && !newInfo.getSurname().isEmpty()) {
            user.setSurname(newInfo.getSurname().trim());
        }
        if (newInfo.getCar() != null && !newInfo.getCar().isEmpty()) {
            user.setCar(newInfo.getCar().trim());
        }

        userRepository.saveAndFlush(user);
    }
}
