package com.smrt.smartdiagnostics.Steps;

import com.smrt.smartdiagnostics.Controllers.RegisterController;
import com.smrt.smartdiagnostics.Repositories.UserRepository;
import com.smrt.smartdiagnostics.Services.UserService;
import com.smrt.smartdiagnostics.Services.VerificationService;
import com.smrt.smartdiagnostics.Models.User;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class registrationSteps {
    private UserService userService;
    private UserRepository userRepository;
    private VerificationService verificationService;
    private JavaMailSender mailSender;
    private RegisterController registrationController;
    private ResponseEntity response;

    public registrationSteps() {
        userService = mock(UserService.class);
        verificationService = mock(VerificationService.class);
        mailSender = mock(JavaMailSender.class);
        userRepository = mock(UserRepository.class);
        registrationController = new RegisterController(userService, verificationService, mailSender);
    }

    @Given("the visitor navigates to the registration page")
    public void theVisitorNavigatesToTheRegistrationPage() {
        // This step is mainly for navigation purpose, no action required here.
    }

    @When("^the visitor submits valid registration details with email \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void theVisitorSubmitsValidRegistrationDetails(String email, String password) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(password);
        response = registrationController.submitUser(newUser);
    }

    @Then("^the registration should be successful$")
    public void theRegistrationShouldBeSuccessful() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Given("^a user with email \"([^\"]*)\" already exists$")
    public void aUserWithEmailAlreadyExists(String email) {
        User existingUser = new User();
        existingUser.setEmail(email);
        when(userRepository.existsUserByEmail(existingUser.getEmail())).thenReturn(false);
    }

    @When("^a new visitor attempts to register with existing email \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void aNewVisitorAttemptsToRegister(String email, String password) {
        User existingUser = new User();
        existingUser.setEmail(email);
        existingUser.setPassword(password);
        doThrow(new IllegalStateException()).when(userService).createUser(existingUser);
        response = registrationController.submitUser(existingUser);
    }

    @Then("^the registration should fail$")
    public void theRegistrationShouldFail() {
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }
}
