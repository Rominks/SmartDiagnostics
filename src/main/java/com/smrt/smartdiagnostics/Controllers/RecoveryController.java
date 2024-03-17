package com.smrt.smartdiagnostics.Controllers;

import com.smrt.smartdiagnostics.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/smrt/recovery")
public class RecoveryController {
    private String email = null;
    private final UserService userService;

    @Autowired
    public RecoveryController(UserService userService) {
        this.userService = userService;
    }
}
