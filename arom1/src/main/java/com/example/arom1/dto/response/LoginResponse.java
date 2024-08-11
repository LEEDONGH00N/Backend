package com.example.arom1.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class LoginResponse {
    private String email;
    private String accessToken;
    private String refreshToken;

    @Builder
    public LoginResponse(String email, String accessToken, String refreshToken) {
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
