package com.***REMOVED***.smartdiagnostics.Controllers;

import com.***REMOVED***.smartdiagnostics.Services.UserService;
import com.***REMOVED***.smartdiagnostics.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/***REMOVED***/register")
public class RegisterController {
    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/confirm")
    public void submitUser(@RequestBody User user) {
        try {
            userService.createUser(user);
            sendVerification(user.getEmail());
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
