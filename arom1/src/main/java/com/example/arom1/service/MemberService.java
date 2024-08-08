package com.example.arom1.service;

import com.example.arom1.common.exception.BaseException;
import com.example.arom1.common.response.BaseResponseStatus;
import com.example.arom1.common.util.jwt.TokenProvider;
import com.example.arom1.dto.request.LoginRequest;
import com.example.arom1.dto.request.SignupRequest;
import com.example.arom1.dto.response.LoginResponse;
import com.example.arom1.entity.Member;
import com.example.arom1.entity.security.MemberDetail;
import com.example.arom1.entity.security.RefreshToken;
import com.example.arom1.repository.MemberRepository;
import com.example.arom1.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member saveMember(SignupRequest request) {
        Member member = Member.createMember(request, passwordEncoder);

        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new BaseException(BaseResponseStatus.EXIST_EMAIL);
        }

        return memberRepository.save(member);
    }
    //로그인 필터 -> 로그인 서비스 메서드 변경 (이유: 예외 처리 수월 등)
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            MemberDetail memberDetail = (MemberDetail) authentication.getPrincipal();
            Long memberId = memberDetail.getId();

            RefreshToken refreshToken = RefreshToken.of(tokenProvider.generateRefreshToken(memberDetail), memberId);
            //DB에 있는 리프레시 토큰 삭제 후 저장
            refreshTokenRepository.deleteByMemberId(memberDetail.getId());
            refreshTokenRepository.save(refreshToken);

            return LoginResponse.builder()
                    .email(memberDetail.getUsername())
                    .accessToken(tokenProvider.generateAccessToken(memberDetail.getId(), memberDetail.getUsername()))
                    .refreshToken(refreshToken.getRefreshToken())
                    .build();

        } catch (AuthenticationException e) {
            System.out.println("AuthenticationException : " + e.getMessage());
            throw new BaseException(BaseResponseStatus.NON_EXIST_USER);
        }
    }

    public String reissueAccessToken(String refreshToken, HttpServletRequest request) {
        tokenProvider.validateToken(refreshToken);

        String expiredAccessToken = tokenProvider.resolveToken(request);

        RefreshToken originalToken = refreshTokenRepository.findByMemberId(tokenProvider.getUserId(expiredAccessToken))
                .orElseThrow(() -> new BaseException(BaseResponseStatus.FAIL_TOKEN_AUTHORIZATION));

        //액세스 토큰의 멤버와 리프레시 토큰의 멤버가 일치하는지 확인
        if(refreshToken.equals(originalToken.getRefreshToken())) {
            return tokenProvider.generateAccessToken(
                    tokenProvider.getUserId(expiredAccessToken),
                    tokenProvider.getEmail(expiredAccessToken));
        }
        throw new BaseException(BaseResponseStatus.FAIL_TOKEN_AUTHORIZATION);

    }

    public void logout(MemberDetail memberDetail) {
        refreshTokenRepository.deleteByMemberId(memberDetail.getId());
    }


}
