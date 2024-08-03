package com.example.arom1.common.filter;

import com.example.arom1.common.exception.BaseException;
import com.example.arom1.common.response.BaseResponseStatus;
import com.example.arom1.common.util.jwt.TokenProvider;
import com.example.arom1.dto.request.LoginRequest;
import com.example.arom1.entity.security.MemberDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public JwtLoginFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        super.setAuthenticationManager(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("LoginFilter 인증 : 진입");

        ObjectMapper objectMapper = new ObjectMapper();
        LoginRequest loginDto = null;
        try {
            loginDto = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);


    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
       String method = request.getMethod();
        if (!method.equals("POST")) {
            throw new BaseException(BaseResponseStatus.HTTP_METHOD_ERROR);
        }
        MemberDetail memberDetail = (MemberDetail) authentication.getPrincipal();

        String jwtToken = tokenProvider.generateToken(memberDetail);

        response.addHeader("Authorization", "Bearer " + jwtToken);

        doFilter(request, response, chain);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        System.out.println("Authentication failed");
        throw new BaseException(BaseResponseStatus.NON_EXIST_USER);
    }


}
