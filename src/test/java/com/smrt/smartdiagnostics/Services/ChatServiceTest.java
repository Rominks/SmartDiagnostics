package com.smrt.smartdiagnostics.Services;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.ProtocolVersion;
import org.apache.http.HttpStatus;
import org.apache.http.HttpEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ChatServiceTest {

    @MockBean
    private CloseableHttpClient httpClient;

    @Autowired
    private ChatService chatService;

    @Test
    public void testCreateAssistant() throws Exception {
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        when(httpClient.execute(any(HttpPost.class))).thenReturn(response);
        String json = "{\"assistantId\": \"asst_FNbpA9pT8lJ9ID3zbwsGERJU\", \"status\": \"Success\"}";
        when(response.getEntity()).thenReturn(new StringEntity(json));

        Map<String, Object> result = chatService.createAssistant("Test Assistant", "Instructions", new ArrayList<>());
        assertEquals("Success", result.get("status"), "Expected status to be Success");
        assertNotNull(result.get("assistantId"), "Assistant ID should not be null");
        assertTrue(result.get("assistantId").toString().matches("asst_[\\w]+"), "Assistant ID format is incorrect");
    }
    @Test
    public void testCreateThread() throws Exception {
        String json = "{\"threadId\": \"thread_HpjuLZXhraMXCQ93mLWPvgcW\", \"status\": \"Success\"}";
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        when(response.getEntity()).thenReturn(new StringEntity(json));
        when(httpClient.execute(any(HttpPost.class))).thenReturn(response);

        // Execute the method under test
        Map<String, Object> result = chatService.createThread();

        // Assertions
        // Check that the 'status' field in the response indicates success
        assertEquals("Success", result.get("status"), "Expected status to be Success");

        // Check that a 'threadId' is present in the response and matches the expected format
        assertNotNull(result.get("threadId"), "Thread ID should not be null");
        assertTrue(result.get("threadId").toString().matches("thread_[\\w]+"), "Thread ID format is incorrect");
    }
    @Test
    public void testSendMessageToThread() throws Exception {
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        when(httpClient.execute(any(HttpPost.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(new StringEntity("{\"status\": \"Success\"}"));

        Map<String, Object> result = chatService.sendMessageToThread("Hello, World!", "asst_lxeO7cCuu4kZdkmrei22i3rC", "thread_6ImdQY1Y1bKRf6dRGudCPIua");
        assertEquals("Success", result.get("status"));
    }
    @Test
    public void testGetMessages() throws Exception {
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        when(httpClient.execute(any(HttpGet.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(new StringEntity("{\"messages\": [{\"message\": \"Hello, World!\"}]}"));

        Map<String, Object> result = chatService.getMessages("thread_6ImdQY1Y1bKRf6dRGudCPIua");
        assertNotNull(result.get("messages"));
    }
}
