package com.smrt.smartdiagnostics.Controllers;

import com.smrt.smartdiagnostics.Models.Recovery;
import com.smrt.smartdiagnostics.Models.User;
import com.smrt.smartdiagnostics.Models.Verification;
import com.smrt.smartdiagnostics.Services.RecoveryService;
import com.smrt.smartdiagnostics.Services.UserService;
import com.smrt.smartdiagnostics.Services.VerificationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
@RestController
@RequestMapping("/smrt/recovery")
public class RecoveryController {

    @Value("${SMTP_USER}")
    private String email = null;
    @Value("${BASE_IP}")
    private String BASE_IP;
    private final UserService userService;
    private final JavaMailSender mailSender;
    private final RecoveryService recoveryService;

    @Autowired
    public RecoveryController(UserService userService, JavaMailSender mailSender, RecoveryService recoveryService) {
        this.userService = userService;
        this.mailSender = mailSender;
        this.recoveryService = recoveryService;
    }

    @PostMapping("/reset/{identity}")
    public ResponseEntity<String> resetPassword(@PathVariable("identity") String identity) {
        System.out.println("Processing reset password for: " + identity);
        Optional<User> user = userService.getUserByEmail(identity)
                .or(() -> userService.getUserByUsername(identity));
        if (user.isPresent()) {
            String code = generatePasswordCode();
            recoveryService.saveVerification(new Recovery(user.get().getEmail(), code, LocalDateTime.now()));

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(email);
            mailMessage.setTo(user.get().getEmail());
            mailMessage.setSubject("Password Reset Request");
            String link = "http://" + BASE_IP + ":8080/reset_password.html?code=" + code; // Ensure this path is correct
            mailMessage.setText("Hello,\n\nTo reset your password, please follow this link:\n" + link);

            mailSender.send(mailMessage);
            return ResponseEntity.ok("Reset email sent to " + user.get().getEmail());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user found.");
        }
    }


    public static String generatePasswordCode() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 25;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }


    @PostMapping("/changepassword")
    public void changePassword(@RequestParam("code") String code, @RequestParam("password") String password, HttpServletResponse response) throws IOException, IOException {
        try {
            String email = recoveryService.getEmailByCode(code);
            Optional<User> user = userService.getUserByEmail(email);
            if (user.isPresent()) {
                user.get().setPassword(password); // Make sure to hash this password
                userService.updateUser(user.get());
                recoveryService.confirmVerification(code);
                response.sendRedirect("/password_reset_success.html");
            } else {
                response.sendRedirect("/password_reset_failure.html");
            }
        } catch (Exception e) {
            response.sendRedirect("/password_reset_failure.html");
        }
    }
    //create get request that would get username by code
    @GetMapping("/getusername/{code}")
    public String getUsernameByCode(@PathVariable("code") String code) {
        String email = recoveryService.getEmailByCode(code);
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isPresent()) {
            return user.get().getEmail();
        }else{
            return null;
        }

    }
}
