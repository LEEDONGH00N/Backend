package com.example.arom1.controller;

import com.example.arom1.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/hello")
    public void message(MessageDto messageDto){
        simpMessageSendingOperations.convertAndSend("/sub/chatroom/" + messageDto.getChatroomId(), messageDto);
    }
}
