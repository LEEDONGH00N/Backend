package com.example.arom1.dto.response;


import com.example.arom1.entity.Member;
import lombok.Builder;

import java.util.List;

public class SignupResponse {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String introduction;
    private int age;
    private String nickname;

    private List<String> errorMessages;

    @Builder
    private SignupResponse(Long id, String email, String password, String name, String introduction, int age, String nickname, List<String> errorMessages) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.introduction = introduction;
        this.age = age;
        this.nickname = nickname;
        this.errorMessages = errorMessages;
    }

    public static SignupResponse MemberToSignupResponse(Member member) {
        return SignupResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .name(member.getName())
                .introduction(member.getIntroduction())
                .age(member.getAge())
                .nickname(member.getNickname())

                .build();
    }

    public static SignupResponse ErrorMessagesToSignupResponse(List<String> errorMessages) {
        return SignupResponse.builder()
                .errorMessages(errorMessages)
                .build();
    }

}
