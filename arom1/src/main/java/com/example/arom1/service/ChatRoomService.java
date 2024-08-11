package com.example.arom1.service;

import com.example.arom1.common.exception.BaseException;
import com.example.arom1.common.response.BaseResponseStatus;
import com.example.arom1.entity.ChatRoom;
import com.example.arom1.entity.Meeting;
import com.example.arom1.repository.ChatRoomRepository;
import com.example.arom1.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatRoomService {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    public Long createChatRoom(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_MEETING));

        // 채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomName("Chat Room for Meeting " + meeting.getTitle())
                .totalMembers(meeting.getMeeting_max_member())
                .participants(meeting.getMeeting_participated_member())
                .meeting(meeting) // 회의와 연결
                .build();

        // 채팅방 저장
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return savedChatRoom.getId();
    }

    public List<ChatRoom> findAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    public Optional<ChatRoom> findChatRoomById(Long id) {
        return chatRoomRepository.findById(id);
    }



}
