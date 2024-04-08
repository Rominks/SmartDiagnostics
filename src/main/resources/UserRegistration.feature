Feature: User Registration
  As a visitor
  I want to be able to create a new account
  So that I can access exclusive account features

  Scenario: Successful Registration
    Given the visitor navigates to the registration page
    When the visitor submits valid registration details with email "smrtdiag@gmail.com" and password "asd"
    Then the registration should be successful

  Scenario: Unsuccessful Registration with Already Used Email
    Given a user with email "smrtdiag@gmail.com" already exists
    When a new visitor attempts to register with existing email "smrtdiag@gmail.com" and password "asd"
    Then the registration should fail