package com.example.arom1.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    @NotNull(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotNull(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotNull(message = "이름을 입력해주세요.")
    private String name;

    @NotNull(message = "자기소개를 입력해주세요.")
    private String introduction;

    @NotNull(message = "나이를 입력해주세요.")
    private int age;

    @NotNull(message = "닉네임을 입력해주세요.")
    private String nickname;


}
