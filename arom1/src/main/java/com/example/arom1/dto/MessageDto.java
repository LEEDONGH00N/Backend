package com.example.arom1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private String type;
    private String sender;
    private String chatroomId;
    private String data;

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void newConnect(){
        this.type="new";
    }

    public void closeConnect(){
        this.type="close";
    }
}
