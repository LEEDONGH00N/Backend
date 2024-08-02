package com.example.arom1.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginRequest {
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    @NotNull(message = "비밀번호를 입력해주세요.")
    private String password;
}
