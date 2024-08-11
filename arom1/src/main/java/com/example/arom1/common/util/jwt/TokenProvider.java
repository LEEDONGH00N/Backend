package com.example.arom1.common.util.jwt;

import com.example.arom1.common.exception.BaseException;
import com.example.arom1.common.response.BaseResponseStatus;
import com.example.arom1.entity.security.MemberDetail;
import com.example.arom1.service.MemberDetailService;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;


@RequiredArgsConstructor
@Component
public class TokenProvider {
    private final JwtProperties jwtProperties;
    private final MemberDetailService memberDetailService;

    // 1. JWT 토큰 생성 메서드
    public String generateAccessToken(Long id, String email) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtProperties.getAccessExpiredAt().toMillis()))
                .setSubject(email)
                .claim("id", id)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public String generateRefreshToken(MemberDetail memberDetail) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtProperties.getRefreshExpiredAt().toMillis()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    // 2. JWT 토큰 유효성 검증 메서드
    public void validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) // 비밀값으로 복호화
                    .parseClaimsJws(token);
        }
        //Null token 예외는 IllegalArgumentException 이 담당
        catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException |
               ExpiredJwtException e) {
            System.out.println(e.getMessage());
            throw new BaseException(BaseResponseStatus.FAIL_TOKEN_AUTHORIZATION);
        }
    }

    // 3. 토큰 기반으로 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        MemberDetail memberDetail = memberDetailService.loadUserByUsername(claims.get("sub", String.class));

        return new UsernamePasswordAuthenticationToken(
                memberDetail,
                null, memberDetail.getAuthorities());
    }

    // 4. 토큰 기반으로 유저 ID를 가져오는 메서드
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    public String getEmail(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    //parseClaimsJws 메서드가 exception 던짐
    private Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();
            //만료되어도 claim 반환시키기
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    //request 에서 토큰 가져오는 메서드
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
