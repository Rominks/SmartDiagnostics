package com.smrt.smartdiagnostics.Steps;

import com.smrt.smartdiagnostics.Services.ChatService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class chatingSteps {

    @Autowired
    private ChatService chatService;

    private Map<String, Object> lastResponse;

    @Given("the service is available")
    public void the_service_is_available() {
    }

    @When("I send a message {string} to the assistant in the thread")
    public void i_send_a_message_to_the_assistant_in_the_thread(String message) {
        String assistantId = (String) lastResponse.get("assistantId");
        String threadId = "thread_6ImdQY1Y1bKRf6dRGudCPIua";
        lastResponse = chatService.sendMessageToThread(message, assistantId, threadId);
    }

    @When("an assistant {string} exists")
    public void an_assistant_exists(String assistantId) {
        lastResponse = new HashMap<>();
        lastResponse.put("assistantId", assistantId);
    }

    @When("a thread {string} exists")
    public void a_thread_exists(String threadId) {
        lastResponse.put("threadId", threadId);
    }

    @Then("the response status should be {string}")
    public void the_response_status_should_be(String expectedStatus) {
        assertEquals(expectedStatus, lastResponse.get("status"));
    }
    @Given("I have sent a message {string} to the assistant in the thread")
    public void i_have_sent_a_message_to_the_assistant_in_the_thread(String message) {
        String assistantId = "asst_lxeO7cCuu4kZdkmrei22i3rC";
        String threadId = "thread_6ImdQY1Y1bKRf6dRGudCPIua";
        lastResponse = chatService.sendMessageToThread(message, assistantId, threadId);
        assertNotNull(lastResponse.get("status"), "Message sending failed");
    }

    @When("I request to get all messages from the thread")
    public void i_request_to_get_all_messages_from_the_thread() {
        String threadId = "thread_6ImdQY1Y1bKRf6dRGudCPIua";
        lastResponse = chatService.getMessages(threadId);
    }

    @Then("I should receive at least one message")
    public void i_should_receive_at_least_one_message() {
        assertTrue(((List<?>) lastResponse.get("messages")).size() > 0, "No messages were received");
    }

}
