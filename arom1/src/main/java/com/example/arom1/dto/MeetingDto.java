package com.example.arom1.dto;

import com.example.arom1.entity.Eatery;
import com.example.arom1.entity.Meeting;
import com.example.arom1.entity.Member;
import com.example.arom1.entity.Review;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
public class MeetingDto {
    private long id;

    @NotBlank(message = "제목을 한 글자 이상 입력해주세요.")
    private String title;

    @NotNull
    private LocalDateTime meeting_time;

    @NotNull
    private int meeting_max_member;

    @NotNull
    private int meeting_participated_member;

    @NotNull
    private long member_id;

    @NotNull
    private long eatery_id;


    @Builder
    public MeetingDto(long id, String title, LocalDateTime meeting_time, int meeting_max_member, int meeting_participated_member,long member_id, long eatery_id){
        this.id=id;
        this.title=title;
        this.meeting_time=meeting_time;
        this.meeting_max_member=meeting_max_member;
        this.meeting_participated_member=meeting_participated_member;
        this.member_id=member_id;
        this.eatery_id=eatery_id;
    }



}
