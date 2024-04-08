Feature: Chating feature

  Scenario: Successfully send a message to an existing thread
    Given the service is available
    And an assistant "asst_lxeO7cCuu4kZdkmrei22i3rC" exists
    And a thread "thread_6ImdQY1Y1bKRf6dRGudCPIua" exists
    When I send a message "What's the weather like today?" to the assistant in the thread
    Then the response status should be "Success"

  Scenario: Successfully retrieve messages from an existing thread
    Given the service is available
    And an assistant "asst_lxeO7cCuu4kZdkmrei22i3rC" exists
    And a thread "thread_6ImdQY1Y1bKRf6dRGudCPIua" exists
    And I have sent a message "What's the weather like today?" to the assistant in the thread
    When I request to get all messages from the thread
    Then the response status should be "Success"
    And I should receive at least one message

