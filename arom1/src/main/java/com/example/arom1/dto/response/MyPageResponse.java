package com.example.arom1.dto.response;

import com.example.arom1.entity.Member;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;


@Getter
public class MyPageResponse {
    private String nickname;
    private int age;
    private String introduction;

    private String email;
    private String name;
    @Enumerated(EnumType.STRING)
    private Member.Gender gender;


    @Builder
    private MyPageResponse(String nickname, int age, String introduction, String email, String name, Member.Gender gender) {
        this.nickname = nickname;
        this.age = age;
        this.introduction = introduction;
        this.email = email;
        this.name = name;
        this.gender = gender;
    }

    public static MyPageResponse of(Member member) {
        return MyPageResponse.builder()
                .age(member.getAge())
                .introduction(member.getIntroduction())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .name(member.getName())
                .gender(member.getGender())

                .build();
    }


}
