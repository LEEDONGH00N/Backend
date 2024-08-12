package com.example.arom1.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MyPageRequest {
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;
    @NotNull(message = "나이를 입력해주세요.")
    private int age;
    @NotBlank(message = "자기소개를 입력해주세요.")
    private String introduction;

}
