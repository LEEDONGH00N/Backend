package com.example.arom1.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TokenReissueRequest {
    @NotBlank(message = "리프레시 토큰을 입력해주세요.")
    private String refreshToken;
}
