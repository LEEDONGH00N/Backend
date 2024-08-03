package com.example.arom1.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
public class SignupRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "자기소개를 입력해주세요.")
    private String introduction;

    @NotNull(message = "나이를 입력해주세요.")
    private int age;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;


}
