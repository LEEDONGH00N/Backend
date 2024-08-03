package com.example.arom1.dto.response;

import com.example.arom1.entity.Member;
import lombok.Builder;
import lombok.Getter;


@Getter
public class MyPageResponse {
    private String nickname;
    private int age;
    private String introduction;

    @Builder
    private MyPageResponse(String nickname, int age, String introduction) {
        this.nickname = nickname;
        this.age = age;
        this.introduction = introduction;
    }

    public static MyPageResponse toMyPageResponse(Member member) {
        return MyPageResponse.builder()
                .age(member.getAge())
                .introduction(member.getIntroduction())
                .nickname(member.getNickname())
                .build();
    }


}
