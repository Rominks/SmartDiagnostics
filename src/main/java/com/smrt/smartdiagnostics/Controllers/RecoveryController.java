package com.smrt.smartdiagnostics.Controllers;

import com.smrt.smartdiagnostics.Models.Recovery;
import com.smrt.smartdiagnostics.Models.User;
import com.smrt.smartdiagnostics.Models.Verification;
import com.smrt.smartdiagnostics.Services.RecoveryService;
import com.smrt.smartdiagnostics.Services.UserService;
import com.smrt.smartdiagnostics.Services.VerificationService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity resetPassword(@PathVariable("identity") String identity) {
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
            String code=generatePasswordCode();
            recoveryService.saveVerification(new Recovery(user.get().getEmail(), code, LocalDateTime.now()));
            String mailText = "Greetings. \n to reset your password, please visit the link below: \n";
            mailText += "http://" + BASE_IP + ":80/smrt/recovery/?code="+code;
            mailMessage.setFrom(email);
            mailMessage.setTo(user.get().getEmail());
            mailMessage.setSubject("Password reset");
            mailMessage.setText(mailText);

            mailSender.send(mailMessage);
            System.out.println("Siunciam laiska");
            return new ResponseEntity<>(user, HttpStatus.OK);
        }else {
            System.out.println("Nera tokio userio");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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




    @PostMapping("/changepassword/{code}")
    public ResponseEntity resetPassword(@PathVariable("code") String code, @RequestBody String password) {
        String email = recoveryService.getEmailByCode(code);
        recoveryService.confirmVerification(code);
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isPresent()) {
            user.get().setPassword(password);
            userService.updateUser(user.get());

            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
