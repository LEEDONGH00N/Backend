package com.example.arom1.service;

import com.example.arom1.common.exception.BaseException;
import com.example.arom1.common.response.BaseResponseStatus;
import com.example.arom1.dto.request.LoginRequest;
import com.example.arom1.dto.request.SignupRequest;
import com.example.arom1.dto.response.LoginResponse;
import com.example.arom1.entity.Member;
import com.example.arom1.entity.security.MemberDetail;
import com.example.arom1.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public Member saveMember(SignupRequest request) {
        Member member = Member.createMember(request, passwordEncoder);
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new BaseException(BaseResponseStatus.EXIST_EMAIL);
        }
        return memberRepository.save(member);
    }
    @Transactional
    public void deleteMember(MemberDetail memberDetail) {
        memberRepository.deleteById(memberDetail.getId());
        logout(memberDetail);
        System.out.println("delete member success");
    }

    //로그인 필터 -> 로그인 서비스 메서드 변경 (이유: 예외 처리 수월 등)
    public LoginResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        try {
            //OAuth2에도 쓰이는 로직이므로 login 메서드와 분리
            return refreshTokenService.buildLoginResponse(authenticationManager.authenticate(authenticationToken));
        } catch (AuthenticationException e) {
            System.out.println("AuthenticationException : " + e.getMessage());
            throw new BaseException(BaseResponseStatus.NON_EXIST_USER);
        }
    }

    public void logout(MemberDetail memberDetail) {
        refreshTokenService.deleteRefreshToken(memberDetail);
    }

}
