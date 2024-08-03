package com.example.arom1.controller;

import com.example.arom1.common.exception.BaseException;
import com.example.arom1.common.response.BaseResponse;
import com.example.arom1.dto.request.LoginRequest;
import com.example.arom1.dto.request.SignupRequest;
import com.example.arom1.dto.response.SignupResponse;
import com.example.arom1.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        //에러 있으면 로그 출력 후 에러메시지 출력
        if(bindingResult.hasErrors()) {
            return new BaseResponse<>(false, HttpStatus.BAD_REQUEST.value(), findErrorMessage(bindingResult));
        }

        try {
            return new BaseResponse<>(SignupResponse
                    .MemberToSignupResponse(memberService.saveMember(request)));

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
    @PostMapping("/login")
    public BaseResponse<String> login(@Valid @RequestBody LoginRequest loginRequest,
                                      BindingResult bindingResult, HttpServletResponse response) {
        if(bindingResult.hasErrors()) {
            return new BaseResponse<>(false, HttpStatus.BAD_REQUEST.value(), findErrorMessage(bindingResult));
        }

        try {
            String jwtToken =  memberService.login(loginRequest);
            response.addHeader("Authorization", "Bearer " + jwtToken);
            return new BaseResponse<>("Login Success");
        }
        catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        return "redirect:/login";
    }

    private String findErrorMessage(BindingResult bindingResult) {
        List<String> messages = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error ->  messages.add(error.getDefaultMessage()));
        for(String msg : messages) System.out.println(msg);

        return messages.get(0);
    }

}
