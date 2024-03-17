package com.smrt.smartdiagnostics.Controllers;

import com.smrt.smartdiagnostics.Services.ChatService;
import com.smrt.smartdiagnostics.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/smrt/chat")
public class ChatController {
//    private final User currentUser;
    private final ChatService chatService;

    @Autowired
    public ChatController(/*User currentUser,*/ ChatService chatService) {
//        this.currentUser = currentUser;
        this.chatService = chatService;
    }
}
