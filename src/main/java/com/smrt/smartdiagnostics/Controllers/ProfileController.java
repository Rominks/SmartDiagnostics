package com.***REMOVED***.smartdiagnostics.Controllers;

import com.***REMOVED***.smartdiagnostics.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/***REMOVED***/profile")
public class ProfileController {
    private long userId = -1;
    private final UserService userService;

    @Autowired
    public ProfileController(/*long userId,*/ UserService userService) {
//        this.userId = userId;
        this.userService = userService;
    }

}
