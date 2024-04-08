Feature: User Login
  As a user
  I want to be able to log in to my account
  So that I can access my account features

  Scenario: Successful Login
    Given a user with email "smrtdiag@gmail.com" and password "asd" exists
    When the user submits login credentials
    Then the login should be successful

  Scenario: Unsuccessful Login
    Given a user with email "smrtdiag@gmail.com" and password "asd" exists
    And the user submits invalid login credentials
    Then the login should fail

  Scenario: Database Error
    Given a database error occurs during login process
    When the user submits login credentials
    Then the login should fail
