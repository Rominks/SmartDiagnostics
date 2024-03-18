package com.smrt.smartdiagnostics.Controllers;

import com.smrt.smartdiagnostics.Models.Recovery;
import com.smrt.smartdiagnostics.Models.User;
import com.smrt.smartdiagnostics.Models.Verification;
import com.smrt.smartdiagnostics.Services.RecoveryService;
import com.smrt.smartdiagnostics.Services.UserService;
import com.smrt.smartdiagnostics.Services.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
@RestController
@RequestMapping("/smrt/recovery")
public class RecoveryController {

    private String email = null;
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
    public void resetPassword(@PathVariable("identity") String identity) {
        System.out.println(identity);//do stuff
        Optional<User> user = null;
        if(userService.getUserByEmail(identity).isEmpty()) {
            System.out.println("Pagal email nera, tikrinam pagal username");
            if(userService.getUserByUsername(identity).isEmpty()){
                System.out.println("nera pagal username");
            }else {
                user = userService.getUserByUsername(identity);
            }
        }else {
            user = userService.getUserByEmail(identity);
        }

        if (user !=null) {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            String code=generatePasswordCode(user.get());
            String mailText = "Greetings. \n to reset your password, please visit the link below: \n";
            mailText += "http://localhost:80/smrt/recovery/?code="+code;
            mailMessage.setFrom("smrtdiag@outlook.com");
            mailMessage.setTo(user.get().getEmail());
            mailMessage.setSubject("Password reset");
            mailMessage.setText(mailText);

            mailSender.send(mailMessage);
            System.out.println("Siunciam laiska");
        }else {
            System.out.println("Nera tokio userio");
        }
    }


    private String generatePasswordCode(User user) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 25;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();


        recoveryService.saveVerification(new Recovery(user.getEmail(), generatedString, LocalDateTime.now()));
        return generatedString;
    }



    @PostMapping("/changepassword/{code}")
    public void resetPassword(@PathVariable("code") String code, @RequestBody String password) {
        String email = recoveryService.getEmailByCode(code);
        recoveryService.confirmVerification(code);
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isPresent()) {
            user.get().setPassword(password);
            userService.updateUser(user.get());
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
