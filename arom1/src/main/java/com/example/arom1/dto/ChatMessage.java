package com.example.arom1.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private MessageType messageType;
    private String sender;
    private String chatroomId;
    private String message;

    public enum MessageType{
        ENTER, TALK, LEAVE
    }

}
