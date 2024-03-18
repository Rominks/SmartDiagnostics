package com.***REMOVED***.smartdiagnostics.Controllers;

import com.***REMOVED***.smartdiagnostics.Models.User;
import com.***REMOVED***.smartdiagnostics.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/***REMOVED***/login")
public class LoginController {
    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/confirm")
    public ResponseEntity submitLogin(@RequestBody User user) {
        try {
            user = userService.getUserByCredentials(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (IllegalStateException e) {
            int responseStatus = 406;
            e.printStackTrace();
            return new ResponseEntity<>(e, HttpStatus.valueOf(responseStatus));
        }
    }
}
