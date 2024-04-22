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
import jakarta.servlet.http.HttpServletResponse;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class VerifySteps {
    private UserService userService;
    private VerificationService verificationService;
    private JavaMailSender javaMailSender;
    private LoginController loginController;
    private RegisterController registerController;
    private User user;
    private Verification verification;
    private HttpServletResponse mockResponse;
    private String actualRedirection;

    public VerifySteps() throws IOException {
        this.userService = mock(UserService.class);
        this.verificationService = mock(VerificationService.class);
        this.javaMailSender = mock(JavaMailSender.class);
        this.mockResponse = mock(HttpServletResponse.class);
        this.registerController = new RegisterController(userService, verificationService, javaMailSender);
        this.loginController = new LoginController(userService);
        setupMockResponse();
    }

    private void setupMockResponse() throws IOException {
        doAnswer(invocation -> {
            actualRedirection = (String) invocation.getArguments()[0];
            return null;
        }).when(mockResponse).sendRedirect(anyString());
    }

    @Given("^a user with email \"([^\"]*)\" and password \"([^\"]*)\" exists and verification is sent with code \"([^\"]*)\"$")
    public void aUserExists(String email, String password, String code) {
        user = new User();
        user.setEmail(email);
        user.setPassword(password);

        verification = new Verification();
        verification.setCode(code);
        verification.setEmail(email);

        when(userService.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(verificationService.getEmailByCode(code)).thenReturn(email);
    }

    @When("^the user clicks verify link in the email$")
    public void the_user_clicks_verify_link_in_the_email() throws Exception {
        registerController.verifyUser(verification.getCode(), mockResponse);
    }

    @Then("^the account should be verified$")
    public void accountShouldBeVerified() throws IOException {
        verify(mockResponse).sendRedirect("/verification_success.html");
    }

    @When("the user submits the wrong verification code")
    public void the_user_submits_the_wrong_verification_code() throws Exception {
        when(verificationService.getEmailByCode("wrongcode")).thenReturn(null);
        registerController.verifyUser("wrongcode", mockResponse);
    }

    @Then("^validation should fail$")
    public void validationFail() throws IOException {
        verify(mockResponse).sendRedirect("/verification_failed.html");
    }
}

//  Scenario: Unsuccessful Verification
//    Given a user with email "smrtdiag@gmail.com" and password "asd" exists
//    And the user copied the wrong verification code
//    When the user submits the wrong verification code
//    Then validation should fail