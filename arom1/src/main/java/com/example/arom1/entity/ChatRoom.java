package com.example.arom1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chatroom")
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatRoomName;

    private int totalMembers = 0;

    private int participants = 0;

    @JsonBackReference
    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL)
    private List<ChatRoomMember> chatroomMemberList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eatery_id")
    private Eatery eatery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Meeting meeting;

    @Builder
    public ChatRoom(Long id, String chatRoomName, int totalMembers, int participants, Meeting meeting) {
        this.id = id;
        this.chatRoomName = chatRoomName;
        this.totalMembers = totalMembers;
        this.participants = participants;
        this.meeting=  meeting;
    }

    public void setMeeting(Meeting meeting){
        this.meeting = meeting;
    }

}