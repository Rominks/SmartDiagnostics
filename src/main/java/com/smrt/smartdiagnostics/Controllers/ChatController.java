package com.***REMOVED***.smartdiagnostics.Controllers;

import com.***REMOVED***.smartdiagnostics.Services.ChatService;
import com.***REMOVED***.smartdiagnostics.dto.CreateAssistantRequest;
import com.***REMOVED***.smartdiagnostics.dto.SendMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("***REMOVED***/chat")
@Slf4j
public class ChatController {

    @Autowired
    ChatService chatGPTAssistantService;

    @PostMapping("/createAssistant")
    public ResponseEntity<?> createAssistant(@RequestBody CreateAssistantRequest createAssistantRequest) {

        Map<String, Object> responseMap = chatGPTAssistantService.createAssistant(createAssistantRequest.getName(), createAssistantRequest.getInstructions(), createAssistantRequest.getTools());

        return new ResponseEntity(responseMap, HttpStatus.OK);
    }

    @PostMapping("/createThread")
    public ResponseEntity<?> createThread() {
        Map<String, Object> responseMap = chatGPTAssistantService.createThread();

        return new ResponseEntity(responseMap, HttpStatus.OK);
    }


    @PostMapping("/sendMessage/{assistantId}/{threadId}")

    public ResponseEntity<?> sendMessage(@RequestBody SendMessageRequest sendMessageRequest, @PathVariable String assistantId, @PathVariable String threadId) {
        Map<String, Object> responseMap = chatGPTAssistantService.sendMessageToThread(sendMessageRequest.getMessage(), assistantId, threadId);

        return new ResponseEntity(responseMap, HttpStatus.OK);
    }

    @GetMapping("/messages/{threadId}")
    public ResponseEntity<?> getMessages(@PathVariable String threadId) {
        Map<String, Object> responseMap = chatGPTAssistantService.getMessages(threadId);

        return new ResponseEntity(responseMap, HttpStatus.OK);
    }

}