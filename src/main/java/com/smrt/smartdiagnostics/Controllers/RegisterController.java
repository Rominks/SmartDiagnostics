package com.smrt.smartdiagnostics.Controllers;

import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.smrt.smartdiagnostics.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/smrt/register")
public class RegisterController {
    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/confirm")
    public void createUser(@RequestBody JSONWrappedObject object) {
        System.out.println(object);
    }
}
