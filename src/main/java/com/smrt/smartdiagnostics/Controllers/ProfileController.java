package com.smrt.smartdiagnostics.Controllers;

import com.smrt.smartdiagnostics.Models.User;
import com.smrt.smartdiagnostics.Services.UserService;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/smrt/profile")
public class ProfileController {
    private final UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable("id") long userId) {
        userService.deleteUser(userId);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateUser(@PathVariable("id") long userId, @RequestBody User newInfo) {
        try {
            userService.updateCredentials(userId, newInfo);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JDBCConnectionException jdbc) {
            jdbc.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
