package com.example.arom1.controller;


import com.example.arom1.common.exception.BaseException;
import com.example.arom1.common.response.BaseResponse;
import com.example.arom1.common.response.BaseResponseStatus;
import com.example.arom1.dto.MeetingDto;
import com.example.arom1.dto.ReviewDto;
import com.example.arom1.dto.response.MeetingResponse;
import com.example.arom1.dto.response.ReviewResponse;
import com.example.arom1.entity.Meeting;
import com.example.arom1.entity.MemberDetail;
import com.example.arom1.entity.Review;
import com.example.arom1.repository.MeetingRepository;
import com.example.arom1.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/eatery")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;
    @Autowired
    private MeetingRepository meetingRepository;

    //음식점 선택 후 식사 메이트 구하는 글 보기
    @GetMapping("/{eateryId}/meetings")
    public BaseResponse<?> getMeetingsByEateryId(@PathVariable long eateryId) throws IOException {
        try{
            List<MeetingResponse> meetingResponseList = meetingService.getMeetingByEateryId(eateryId);
            return new BaseResponse<>(meetingResponseList);
        }
        catch(BaseException e){
            return new BaseResponse<>(e);
        }
        catch(Exception e){
            e.printStackTrace();
            return new BaseResponse<>(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }
    }

    //음식점 식사 메이트 구하는 글 작성
    @PostMapping("/{eateryId}/meetings")
    public BaseResponse<?> saveMeeting(@PathVariable long eateryId, @RequestBody MeetingDto meetingDto){
        try{
            // 저장된 회의 정보를 반환
            MeetingResponse meetingResponse = meetingService.saveMeeting(meetingDto);
            return new BaseResponse<>(meetingResponse);
        }catch (BaseException e) {
            // 로그에 스택 트레이스 출력
            e.printStackTrace();
            return new BaseResponse<>(e.getStatus());
        } catch (Exception e) {
            // 일반 예외 처리
            e.printStackTrace();
            return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    //수정
    @PutMapping("/{eateryId}/meetings/{meetingId}")
    public BaseResponse<?> updateMeeting(@PathVariable Long eateryId, @PathVariable Long meetingId, @RequestBody MeetingDto meetingDto ){
        try{
            MeetingResponse updatedMeeting = meetingService.updateMeeting(eateryId, meetingId, meetingDto);
            return new BaseResponse<>(updatedMeeting);
        }
        catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    //삭제
    @DeleteMapping("/{eateryId}/meetings/{meetingId}")
    public BaseResponse<?> deleteMeeting(@PathVariable Long eateryId, @PathVariable Long meetingId, @RequestBody MeetingDto meetingDto ){
        try{
            meetingService.deleteMeeting(meetingId, meetingDto.getMember_id());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }
        catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    //상세보기 기능
    @GetMapping("/{eateryId}/meetings/{meetingId}")
    public BaseResponse<?> getMeetingById(@PathVariable Long eateryId, @PathVariable Long meetingId){
        try{
            MeetingResponse meetingResponse = meetingService.getMeetingById(meetingId);
            return new BaseResponse<>(meetingResponse);

        }
        catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

}
