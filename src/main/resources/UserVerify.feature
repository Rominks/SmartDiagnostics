Feature: User Verification
  As a user
  I want to verify my account
  So that my account would be verified

  Scenario: Successful Verification
    Given a user with email "smrtdiag@gmail.com" and password "asd" exists and verification is sent with code "123"
    When the user clicks verify link in the email
    Then the account should be verified

  Scenario: Unsuccessful Verification
    Given a user with email "smrtdiag@gmail.com" and password "asd" exists and verification is sent with code "123"
    When the user submits the wrong verification code
    Then validation should fail