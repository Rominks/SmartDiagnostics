package com.smrt.smartdiagnostics.Controllers;

import com.google.gson.Gson;
import com.smrt.smartdiagnostics.Services.ChatService;
import com.smrt.smartdiagnostics.dto.CreateAssistantRequest;
import com.smrt.smartdiagnostics.dto.SendMessageRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    private final Gson gson = new Gson();

    @Test
    public void testCreateAssistant() throws Exception {
        //Given Setup mock service response and request payload
        Map<String, Object> serviceResponse = new HashMap<>();
        serviceResponse.put("status", "Success");
        serviceResponse.put("assistantId", "123");

        given(chatService.createAssistant(anyString(), anyString(), any())).willReturn(serviceResponse);

        CreateAssistantRequest requestPayload = new CreateAssistantRequest();
        requestPayload.setName("Assistant Name");
        requestPayload.setInstructions("Instructions");

        //When Perform the POST request to send a message
        mockMvc.perform(post("/smrt/chat/createAssistant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestPayload)))
        //Then Expect the response to be OK and match the expected JSON structure
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.assistantId").value("123"));
    }

    @Test
    public void testCreateThread() throws Exception {
        //Given
        Map<String, Object> serviceResponse = new HashMap<>();
        serviceResponse.put("status", "Success");
        serviceResponse.put("threadId", "456");

        given(chatService.createThread()).willReturn(serviceResponse);
        //When
        mockMvc.perform(post("/smrt/chat/createThread")
                        .contentType(MediaType.APPLICATION_JSON))
        //Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.threadId").value("456"));
    }

    @Test
    public void testSendMessage() throws Exception {
        //Given
        SendMessageRequest requestPayload = new SendMessageRequest();
        requestPayload.setMessage("Test message");

        Map<String, Object> serviceResponse = new HashMap<>();
        serviceResponse.put("status", "Success");
        serviceResponse.put("message", "Message received");
        given(chatService.sendMessageToThread(anyString(), anyString(), anyString())).willReturn(serviceResponse);

        //When
        mockMvc.perform(post("/smrt/chat/sendMessage/{assistantId}/{threadId}", "123", "456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(requestPayload)))
        // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.message").value("Message received"));
    }

    @Test
    public void testGetMessages() throws Exception {
        //Given
        Map<String, Object> serviceResponse = new HashMap<>();
        serviceResponse.put("status", "Success");
        Map<String, String> messageDetails = new HashMap<>();
        messageDetails.put("value", "Hello, World!");
        serviceResponse.put("messages", new Map[]{messageDetails});

        given(chatService.getMessages(anyString())).willReturn(serviceResponse);
        //When
        mockMvc.perform(get("/smrt/chat/messages/{threadId}", "456")
                        .accept(MediaType.APPLICATION_JSON))
        //Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.messages[0].value").value("Hello, World!"));
    }
}
