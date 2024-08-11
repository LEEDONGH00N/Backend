package com.example.arom1.controller;

import com.example.arom1.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/chat/{id}/Message")
    public void sendMessage(@DestinationVariable("id") Long id, ChatMessage chatMessage) {
        if (isJoin(chatMessage)) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장하였습니다");
        }
        else if (isLeave(chatMessage)) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 퇴장하였습니다");
        }

        // 특정 채팅방에 메시지를 브로드캐스트
        simpMessageSendingOperations.convertAndSend("/sub/chat/" + chatMessage.getChatroomId(), chatMessage);
    }

    private boolean isJoin(ChatMessage message) {
        return message.getMessageType().equals(ChatMessage.MessageType.ENTER);
    }

    private boolean isLeave(ChatMessage message) {
        return message.getMessageType().equals(ChatMessage.MessageType.LEAVE);
    }

}
