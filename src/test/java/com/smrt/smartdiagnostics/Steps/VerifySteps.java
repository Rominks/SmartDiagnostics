package com.smrt.smartdiagnostics.Steps;

import com.smrt.smartdiagnostics.Controllers.LoginController;
import com.smrt.smartdiagnostics.Controllers.RegisterController;
import com.smrt.smartdiagnostics.Models.User;
import com.smrt.smartdiagnostics.Models.Verification;
import com.smrt.smartdiagnostics.Repositories.UserRepository;
import com.smrt.smartdiagnostics.Services.UserService;
import com.smrt.smartdiagnostics.Services.VerificationService;
import com.smrt.smartdiagnostics.SmartDiagnosticsApplication;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class VerifySteps {
    private UserService userService;
    private VerificationService verificationService;
    private JavaMailSender javaMailSender;
    private UserRepository userRepository;
    private LoginController loginController;
    private RegisterController registerController;
    private User user;
    private Verification verification;
    private ResponseEntity response;

    public VerifySteps() {
        userRepository = mock(UserRepository.class);
        this.userService = mock(UserService.class);
        this.verificationService= mock(VerificationService.class);
        this.javaMailSender = mock(JavaMailSender.class);
        registerController = new RegisterController(userService,verificationService,javaMailSender);
        loginController = new LoginController(userService);
    }

    @Given("^a user with email \"([^\"]*)\" and password \"([^\"]*)\" exists and verification is sent with code \"([^\"]*)\"$")
    public void aUserExists(String email, String password, String code) {

        user = new User();
        user.setEmail(email);
        user.setPassword(password);

        verification =new Verification();
        verification.setCode(code);
        verification.setEmail(email);
        //log verification.getEmail();
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(verificationService.getEmailByCode(code)).thenReturn(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(userService.getUserByCredentials(user)).thenReturn(Optional.of(user));
    }

    @When("^the user clicks verify link in the email$")
    public void the_user_clicks_verify_link_in_the_email() {
//        loginController = new LoginController(userService);
        registerController = new RegisterController(userService,verificationService,javaMailSender);
        response = registerController.verifyUser(verification.getCode());
    }

    @Then("^the account should be verified$")
    public void accountShouldBeVerified() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @When("the user submits the wrong verification code")
    public void the_user_submits_the_wrong_verification_code() {
        verification.setCode("wrongcode");
        response = registerController.verifyUser(verification.getCode());
    }

    @Then("^validation should fail$")
    public void validationFail() {
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}

//  Scenario: Unsuccessful Verification
//    Given a user with email "smrtdiag@gmail.com" and password "asd" exists
//    And the user copied the wrong verification code
//    When the user submits the wrong verification code
//    Then validation should fail