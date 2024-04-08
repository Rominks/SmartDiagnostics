package com.smrt.smartdiagnostics.Steps;

import com.smrt.smartdiagnostics.Controllers.LoginController;
import com.smrt.smartdiagnostics.Models.User;
import com.smrt.smartdiagnostics.Services.UserService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class loginSteps {
    private final UserService userService;
    private LoginController loginController;
    private User user;
    private ResponseEntity response;

    public loginSteps() {
        this.userService = mock(UserService.class);
        loginController = new LoginController(userService);
    }

    @Given("^a user with email \"([^\"]*)\" and password \"([^\"]*)\" exists$")
    public void aUserExists(String email, String password) {
        user = new User();
        user.setEmail(email);
        user.setPassword(password);

        when(userService.getUserByCredentials(user)).thenReturn(Optional.of(user));
    }

    @Given("^a database error occurs during login process$")
    public void databaseErrorOccurs() {
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("test");

        doThrow(new IllegalStateException()).when(userService).getUserByCredentials(user);
    }
    @And("the user submits invalid login credentials")
    public void theUserSubmitsInvalidLoginCredentials() {
        User user = new User();
        user.setEmail("invalid@example.com");
        user.setPassword("invalidPassword");
        when(userService.getUserByCredentials(user)).thenReturn(Optional.empty());

        loginController = new LoginController(userService);
        response = loginController.submitLogin(user);
    }

    @When("^the user submits login credentials$")
    public void userSubmitsLoginCredentials() {
        loginController = new LoginController(userService);
        response = loginController.submitLogin(user);
    }

    @Then("^the login should be successful$")
    public void loginShouldBeSuccessful() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Then("^the login should fail$")
    public void loginShouldFail() {
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }
}
