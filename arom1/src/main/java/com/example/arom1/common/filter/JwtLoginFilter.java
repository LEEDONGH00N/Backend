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
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtLoginFilter extends OncePerRequestFilter {

//    private final AuthenticationManager authenticationManager;
//    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        if (!request.getMethod()
//                .equals("POST")) {
//            throw new BaseException(BaseResponseStatus.HTTP_METHOD_ERROR);
//        }
//
//        System.out.println("LoginFilter 인증 : 진입");
//
//        LoginRequest loginRequest = null;
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
//
//        try {
//            authenticationManager.authenticate(authenticationToken);
//            MemberDetail memberDetail = (MemberDetail) authenticationToken.getPrincipal();
//            String jwtToken = tokenProvider.generateToken(memberDetail);
//
//            response.addHeader("Authorization", "Bearer " + jwtToken);
//        } catch (AuthenticationException e) {
//            throw new BaseException(BaseResponseStatus.NON_EXIST_USER);
//        }
//
//        filterChain.doFilter(request, response);

    }


}
