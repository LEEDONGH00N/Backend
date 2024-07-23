package com.example.arom1.service;

import com.example.arom1.common.exception.BaseException;
import com.example.arom1.common.response.BaseResponse;
import com.example.arom1.common.response.BaseResponseStatus;
import com.example.arom1.dto.MeetingDto;
import com.example.arom1.dto.ReviewDto;
import com.example.arom1.dto.response.MeetingResponse;
import com.example.arom1.dto.response.MemberResponse;
import com.example.arom1.dto.response.ReviewResponse;
import com.example.arom1.entity.*;
import com.example.arom1.repository.ChatRoomRepository;
import com.example.arom1.repository.EateryRepository;
import com.example.arom1.repository.MeetingRepository;
import com.example.arom1.repository.MemberRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final EateryRepository eateryRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;


    @Transactional
    // 음식점 선택 후 식사 메이트 구하는 글 보기
    public List<MeetingResponse> getMeetingByEateryId(Long eateryId) {
        List<Meeting> meetings = meetingRepository.findByEateryId(eateryId);

        return meetings.stream().map(meeting -> {
            Member member = memberRepository.findById(meeting.getMember().getId())
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_MEMBER));
            MemberResponse memberResponse = MemberResponse.fromEntity(member);

            // 첫 번째 채팅방의 ID를 가져오기 전에 리스트가 비어있지 않은지 확인
            long chatRoomId = meeting.getChatRooms().isEmpty() ? -1 : meeting.getChatRooms().get(0).getId();

            return MeetingResponse.entityToDto(meeting, memberResponse, chatRoomId);
        }).collect(Collectors.toList());
    }


    //게시글 삭제
    @Transactional
    public void deleteMeeting(Long meetingId, Long memberId) {

        Meeting existingMeeting = meetingRepository.findById(meetingId)
                .orElseThrow(()->new BaseException(BaseResponseStatus.INVALID_MEETING));

        //권한 확인
        if (!existingMeeting.getMember().getId().equals(memberId)) {
            throw new BaseException(BaseResponseStatus.INVALID_MEMBER);
        }

        meetingRepository.delete(existingMeeting);
    }

    //음식점 식사 메이트 구하는 글 수정
    public MeetingResponse updateMeeting(Long eateryId, Long meetingId,  MeetingDto meetingDto) {
        Meeting existingMeeting = meetingRepository.findById(meetingId)
                .orElseThrow(()->new BaseException(BaseResponseStatus.INVALID_MEETING));

        Member member = memberRepository.findById(meetingDto.getMember_id())
                .orElseThrow(()->new BaseException(BaseResponseStatus.INVALID_MEMBER));

        //권한 확인
        if (!existingMeeting.getMember().getId().equals(meetingDto.getMember_id())) {
            throw new BaseException(BaseResponseStatus.INVALID_MEMBER);
        }

        existingMeeting.setTitle(meetingDto.getTitle());
        existingMeeting.setMeeting_time(meetingDto.getMeeting_time());
        existingMeeting.setMeeting_max_member(meetingDto.getMeeting_max_member());
        existingMeeting.setMeeting_participated_member(meetingDto.getMeeting_participated_member());

        Eatery eatery = eateryRepository.findById(meetingDto.getEatery_id())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_EATERY));
        existingMeeting.setEatery(eatery);

        meetingRepository.save(existingMeeting);

        MemberResponse memberResponse = MemberResponse.fromEntity(member);

        // 첫 번째 채팅방의 ID를 가져오기 전에 리스트가 비어있지 않은지 확인
        long chatRoomId = existingMeeting.getChatRooms().isEmpty() ? -1 : existingMeeting.getChatRooms().get(0).getId();


        return MeetingResponse.entityToDto(existingMeeting, memberResponse, chatRoomId);
    }


    public MeetingResponse getMeetingById(Long meetingId){
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_MEETING));
        Member member = memberRepository.findById(meeting.getMember().getId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_MEMBER));
        MemberResponse memberResponse = MemberResponse.fromEntity(member);

        // 첫 번째 채팅방의 ID를 가져오기 전에 리스트가 비어있지 않은지 확인
        long chatRoomId = meeting.getChatRooms().isEmpty() ? -1 : meeting.getChatRooms().get(0).getId();

        return MeetingResponse.entityToDto(meeting, memberResponse, chatRoomId);
    }


    //글 작성 기능
    public MeetingResponse saveMeeting(MeetingDto meetingDto) {
        Eatery eatery = eateryRepository.findById(meetingDto.getEatery_id())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_EATERY));

        Member member = memberRepository.findById(meetingDto.getMember_id())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_MEMBER));

        Meeting meeting = Meeting.newMeeting(meetingDto, member, eatery);
        meetingRepository.save(meeting); // 미팅 업데이트

        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomName("Chat for " + meeting.getTitle())
                .totalMembers(meeting.getMeeting_max_member())
                .participants(meeting.getMeeting_participated_member())
                .meeting(meeting)
                .build();
        chatRoomRepository.save(chatRoom);

        meeting.getChatRooms().add(chatRoom);
        meetingRepository.save(meeting); // 미팅 업데이트

        MemberResponse memberResponse = MemberResponse.fromEntity(member);
        return MeetingResponse.entityToDto(meeting, memberResponse, chatRoom.getId());
    }


}