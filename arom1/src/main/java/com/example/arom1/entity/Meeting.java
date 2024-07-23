package com.example.arom1.entity;

import com.example.arom1.dto.MeetingDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "meeting")
public class Meeting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDateTime meeting_time;

    private int meeting_max_member;

    private int meeting_participated_member;

    @ManyToOne(fetch = FetchType.LAZY) //식당 추가
    @JoinColumn(name = "eatery_id")
    private Eatery eatery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

//    @OneToMany(mappedBy = "meeting")
//    private List<MeetingReply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "meeting", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @Builder
    private Meeting(Long id, String title, LocalDateTime meeting_time, int meeting_max_member, int meeting_participated_member,Member member, Eatery eatery, List<ChatRoom> chatRooms) { //수정
        this.id = id;
        this.title = title;
        this.meeting_time=meeting_time;
        this.meeting_max_member = meeting_max_member;
        this.meeting_participated_member = meeting_participated_member;
        this.member = member;
        this.eatery = eatery;
        if(chatRooms != null) {
            this.chatRooms.addAll(chatRooms);
        }

    }

    public static Meeting newMeeting(MeetingDto meetingDto, Member member, Eatery eatery) {
        Meeting meeting = Meeting.builder()
                .title(meetingDto.getTitle())
                .meeting_time(meetingDto.getMeeting_time())
                .meeting_max_member(meetingDto.getMeeting_max_member())
                .meeting_participated_member(meetingDto.getMeeting_participated_member())
                .member(member)
                .eatery(eatery)
                .build();

        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomName(meeting.getTitle() + "채팅방")
                .meeting(meeting)
                .participants(meeting.getMeeting_participated_member())
                .totalMembers(meeting.getMeeting_max_member())
                .build();
        meeting.getChatRooms().add(chatRoom);

        return meeting;
    }

    public void setChatRooms(List<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMeeting_max_member(int meeting_max_member) {
        this.meeting_max_member = meeting_max_member;
    }

    public void setMeeting_participated_member(int meeting_participated_member) {
        this.meeting_participated_member = meeting_participated_member;
    }

    public void setMeeting_time(LocalDateTime meeting_time) {
        this.meeting_time = meeting_time;
    }

    public void setEatery(Eatery eatery) {
        this.eatery=eatery;
    }

}