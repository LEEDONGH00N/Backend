package com.example.arom1.common.filter;

import com.example.arom1.common.exception.BaseException;
import com.example.arom1.common.response.BaseResponseStatus;
import com.example.arom1.common.util.jwt.TokenProvider;
import com.example.arom1.dto.LoginDto;
import com.example.arom1.entity.MemberDetail;
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

import java.io.IOException;

@RequiredArgsConstructor
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("LoginFilter 인증 : 진입");

        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = null;
        try {
            loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
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
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        System.out.println("Authentication failed");
        throw new BaseException(BaseResponseStatus.NON_EXIST_USER);
    }


}
