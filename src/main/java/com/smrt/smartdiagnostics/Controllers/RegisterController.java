package com.smrt.smartdiagnostics.Controllers;

import com.smrt.smartdiagnostics.Models.User;
import com.smrt.smartdiagnostics.Models.Verification;
import com.smrt.smartdiagnostics.Services.UserService;
import com.smrt.smartdiagnostics.Services.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/smrt/register")
public class RegisterController {
    private final UserService userService;
    private final VerificationService verificationService;
    private final JavaMailSender mailSender;

    @Autowired
    public RegisterController(UserService userService, VerificationService verificationService, JavaMailSender mailSender) {
        this.userService = userService;
        this.verificationService = verificationService;
        this.mailSender = mailSender;
    }

    @PostMapping("/confirm")
    public ResponseEntity submitUser(@RequestBody User user) {
        try {
            userService.createUser(user);
            sendVerification(user);
            return new ResponseEntity<>("Account registered. A verification will be sent to the provided email.", HttpStatus.OK);
        } catch (IllegalStateException e) {
            int responseStatus = 406;
            e.printStackTrace();
            return new ResponseEntity<>("Account registration failed. Either the email or password is in use.", HttpStatus.valueOf(responseStatus));
        }
    }

    @PutMapping("/verify/{code}")
    public ResponseEntity<String> verifyUser(@PathVariable("code") String code) {
        String email = verificationService.getEmailByCode(code);
        if (email == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        verificationService.confirmVerification(code);
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isPresent()) {
            user.get().setVerified(true);
            userService.updateUser(user.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Value("${BASE_IP}")
    private String BASE_IP;

    @Value("${SMTP_USER}")
    private String email;

    private void sendVerification(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String code = generateCode(user);
        String mailText = "Greetings. To verify your account, please visit the link below: \n";
        mailText += "http://" + BASE_IP + "/smrt/register/verify/?code=" + code;
        mailMessage.setFrom(email);
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Verify your account");
        mailMessage.setText(mailText);
        mailSender.send(mailMessage);
    }

    private String generateCode(User user) {
        Random rand = new Random();
        long code = rand.nextLong(50000) + 1;
        verificationService.saveVerification(new Verification(user.getEmail(), String.valueOf(code), LocalDateTime.now()));
        return String.valueOf(code);
    }

}
