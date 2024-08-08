package com.example.arom1.controller;

import com.example.arom1.common.exception.BaseException;
import com.example.arom1.common.response.BaseResponse;
import com.example.arom1.common.response.BaseResponseStatus;
import com.example.arom1.dto.request.LoginRequest;
import com.example.arom1.dto.request.SignupRequest;
import com.example.arom1.dto.request.TokenReissueRequest;
import com.example.arom1.dto.response.LoginResponse;
import com.example.arom1.dto.response.SignupResponse;
import com.example.arom1.entity.security.MemberDetail;
import com.example.arom1.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public BaseResponse<SignupResponse> signup(@Valid @RequestBody SignupRequest request,
                                                 BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return new BaseResponse<>(false, HttpStatus.BAD_REQUEST.value(), findErrorMessage(bindingResult));
        }

        try {
            return new BaseResponse<>(SignupResponse.of(memberService.saveMember(request)));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                      BindingResult bindingResult, HttpServletResponse response) {
        if(bindingResult.hasErrors()) {
            return new BaseResponse<>(false, HttpStatus.BAD_REQUEST.value(), findErrorMessage(bindingResult));
        }

        try {
            LoginResponse loginResponse =  memberService.login(loginRequest);
            response.addHeader("Authorization", "Bearer " + loginResponse.getAccessToken());
            return new BaseResponse<>(loginResponse);
        }
        catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
    @PostMapping("/login/refresh")
    public BaseResponse<String> reissueToken(@Valid @RequestBody TokenReissueRequest tokenReissueRequest,
                                             BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
        if(bindingResult.hasErrors()) {
            return new BaseResponse<>(false, HttpStatus.BAD_REQUEST.value(), findErrorMessage(bindingResult));
        }

        try {
            String newAccessToken = memberService.reissueAccessToken(tokenReissueRequest.getRefreshToken(), request);
            response.addHeader("Authorization", "Bearer " + newAccessToken);
            return new BaseResponse<>("reissue AccessToken Success");
        }
        catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
        catch (IllegalArgumentException e) {
            return new BaseResponse<>(BaseResponseStatus.FAIL_TOKEN_AUTHORIZATION);
        }

    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, auth);
        memberService.logout((MemberDetail) auth.getPrincipal());

        return "redirect:/login";
    }

    private String findErrorMessage(BindingResult bindingResult) {
        List<String> messages = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error ->  messages.add(error.getDefaultMessage()));
        for(String msg : messages) System.out.println(msg);

        return messages.get(0);
    }

}
