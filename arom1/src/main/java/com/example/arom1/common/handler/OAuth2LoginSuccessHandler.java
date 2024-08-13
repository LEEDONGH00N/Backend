package com.example.arom1.common.handler;

import com.example.arom1.common.response.BaseResponse;
import com.example.arom1.dto.response.LoginResponse;
import com.example.arom1.entity.Member;
import com.example.arom1.entity.oauth2.CustomOAuth2User;
import com.example.arom1.service.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Member member = ((CustomOAuth2User) authentication.getPrincipal()).getMember();
        // 최초 로그인인 경우 추가 정보 입력을 위한 회원가입 페이지로 리다이렉트
        if (member.getRole().equals("ROLE_GUEST")) {
            String redirectURL = UriComponentsBuilder.fromUriString("http://localhost:8080/oauth2/signup")
                    .queryParam("email", member.getEmail())
                    .queryParam("socialType", member.getProvider())
                    .queryParam("socialId", member.getProviderId())
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            getRedirectStrategy().sendRedirect(request, response, redirectURL);
        } else {
            BaseResponse<LoginResponse> baseResponse = new BaseResponse<>(refreshTokenService
                    .buildLoginResponse(authentication));
            // HTTP 응답으로 LoginResponse 반환
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(baseResponse));

//            // 최초 로그인이 아닌 경우 로그인 성공 페이지로 이동
//            redirectURL = UriComponentsBuilder.fromUriString("http://localhost:8080/mypage")
//                    .build()
//                    .encode(StandardCharsets.UTF_8)
//                    .toUriString();
        }
    }

}