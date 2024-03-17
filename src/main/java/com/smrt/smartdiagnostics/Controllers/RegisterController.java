package com.smrt.smartdiagnostics.Controllers;

import com.smrt.smartdiagnostics.Models.Verification;
import com.smrt.smartdiagnostics.Services.UserService;
import com.smrt.smartdiagnostics.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Random;

@RestController
@RequestMapping("/smrt/register")
public class RegisterController {
    private final UserService userService;
    private final JavaMailSender mailSender;

    @Autowired
    public RegisterController(UserService userService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }

    @PostMapping("/confirm")
    public void submitUser(@RequestBody User user) {
        try {
            userService.createUser(user);
            sendVerification(user);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void sendVerification(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String code = generateCode(user);
        String mailText = "Greetings. To verify your account, please visit the link below and enter the provided verification code.\n";


        mailText = mailText + "link";

        mailMessage.setFrom("galian2002@gmail.com");
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Verify your account");
        mailMessage.setText(mailText);

        mailSender.send(mailMessage);
    }

    private String generateCode(User user) {
        Random rand = new Random();
        long code = rand.nextLong();
        Verification unverifiedUser = new Verification(user.getEmail(), String.valueOf(code), LocalDateTime.now());
        return "111";
    }

}
