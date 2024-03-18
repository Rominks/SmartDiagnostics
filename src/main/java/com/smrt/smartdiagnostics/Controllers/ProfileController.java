package com.***REMOVED***.smartdiagnostics.Controllers;

import com.***REMOVED***.smartdiagnostics.Models.User;
import com.***REMOVED***.smartdiagnostics.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/***REMOVED***/profile")
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
        }
    }
}
