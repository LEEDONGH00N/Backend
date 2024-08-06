package com.example.arom1.common.filter;

import com.example.arom1.common.response.BaseResponse;
import com.example.arom1.common.response.BaseResponseStatus;
import com.example.arom1.common.util.jwt.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //로그인, 회원가입 시 인증 필터 무시
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/login") || requestURI.equals("/signup")) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("JwtAuthenticationFilter 인증 : 진입");

        String token = tokenProvider.resolveToken(request);

        if (token == null) {
            sendErrorResponse(response, BaseResponseStatus.NO_JWT_TOKEN);
        }
        else if (!tokenProvider.validateToken(token)) {
            sendErrorResponse(response, BaseResponseStatus.INVALID_JWT_TOKEN);
        }
        else {
            Authentication auth = tokenProvider.getAuthentication(token);
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(auth);

            System.out.println("validate success");
            filterChain.doFilter(request, response);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, BaseResponseStatus status) throws IOException {
        System.out.println("validate failed");
        response.setStatus(status.getCode());
        response.setContentType("application/json;charset=UTF-8");

        BaseResponse<String> baseResponse = new BaseResponse<>(status);
        String jsonResponse = objectMapper.writeValueAsString(baseResponse);

        response.getWriter().write(jsonResponse);
    }

}
