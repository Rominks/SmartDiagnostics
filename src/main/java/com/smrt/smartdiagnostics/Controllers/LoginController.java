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

import java.sql.SQLException;

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
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                throw new IllegalStateException("No such user found.");
            }
        } catch (IllegalStateException e) {
            int responseStatus = 406;
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(responseStatus));
        } catch (org.hibernate.exception.JDBCConnectionException jdbc) {
            jdbc.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
