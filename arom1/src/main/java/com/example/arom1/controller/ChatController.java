package com.example.arom1.controller;

import com.example.arom1.common.util.jwt.TokenProvider;
import com.example.arom1.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final TokenProvider tokenProvider;

    @MessageMapping("/chat/message/{chatroomId}") //websocket "pub/chat/message"로 들어오는 메세지 처리
    public void sendMessage(@DestinationVariable Long chatRoomId, ChatMessage chatMessage, @Header("Authorization")String Authorization ) {
        String authorization = tokenProvider.extractJwt(Authorization);
        Object memberId = tokenProvider.getClaims(authorization).get("memberId");
        chatMessage.setSender((String) memberId);

        if(isENTER(chatMessage)){
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장하셨습니다.");
        }

        if(isLEAVE(chatMessage)){
            chatMessage.setMessage(chatMessage.getSender() + "님이 퇴장하셨습니다.");
        }

        // 특정 채팅방에 메시지를 브로드캐스트
        simpMessageSendingOperations.convertAndSend("/sub/chat/room" + chatMessage.getChatroomId(), chatMessage);
    }

    private boolean isENTER(ChatMessage messageType) {
        return messageType.getMessageType().equals(ChatMessage.MessageType.ENTER);
    }

    private boolean isLEAVE(ChatMessage messageType){
        return messageType.getMessageType().equals(ChatMessage.MessageType.LEAVE);
    }
}
