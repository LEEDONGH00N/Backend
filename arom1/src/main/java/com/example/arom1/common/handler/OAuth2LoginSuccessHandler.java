package com.example.arom1.common.handler;

import com.example.arom1.common.response.BaseResponse;
import com.example.arom1.dto.response.LoginResponse;
import com.example.arom1.entity.Member;
import com.example.arom1.entity.oauth2.CustomOAuth2User;
import com.example.arom1.entity.security.MemberDetail;
import com.example.arom1.entity.security.MemberSecurityContext;
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
        System.out.println("OAuth2LoginSuccessHandler : 진입");
        Member member = ((CustomOAuth2User) authentication.getPrincipal()).getMember();

        // 최초 로그인인 경우 회원가입 페이지로 리다이렉트 : 추가 정보 입력
        if (member.getRole().equals("ROLE_GUEST")) {
            System.out.println("최초 로그인 : redirect:/oauth2/signup");
            String redirectURL = UriComponentsBuilder.fromUriString("http://localhost:8080/oauth2/signup")
                    .queryParam("email", member.getEmail())
                    .queryParam("socialType", member.getProvider())
                    .queryParam("socialId", member.getProviderId())
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            getRedirectStrategy().sendRedirect(request, response, redirectURL);
        } else {
            System.out.println("로그인 성공, BaseResponse<LoginResponse> 반환");
            BaseResponse<LoginResponse> baseResponse = new BaseResponse<>(
                    refreshTokenService.buildLoginResponse(
                            new MemberDetail(MemberSecurityContext.of(member))));
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(baseResponse));
            // 최초 로그인이 아닌 경우 로그인 성공 페이지로 이동
//            redirectURL = UriComponentsBuilder.fromUriString("http://localhost:8080/mypage")
//                    .build()
//                    .encode(StandardCharsets.UTF_8)
//                    .toUriString();
        }
    }

}