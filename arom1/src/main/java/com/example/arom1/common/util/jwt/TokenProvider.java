package com.example.arom1.common.util.jwt;

import com.example.arom1.entity.security.MemberDetail;
import com.example.arom1.entity.security.MemberSecurityContext;
import com.example.arom1.repository.MemberRepository;
import com.example.arom1.service.MemberDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class TokenProvider {
    private final JwtProperties jwtProperties;
    private final MemberDetailService memberDetailService;

    // 1. JWT 토큰 생성 메서드
    public String generateToken(MemberDetail memberDetail) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtProperties.getExpiredAt().toMillis()))
                .setSubject(memberDetail.getUsername())
                .claim("id", memberDetail.getId())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    // 2. JWT 토큰 유효성 검증 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) // 비밀값으로 복호화
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
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

//    // 4. 토큰 기반으로 유저 ID를 가져오는 메서드
//    public Long getUserId(String token) {
//        Claims claims = getClaims(token);
//
//        return claims.get("id", Long.class);
//    }

    private Claims getClaims(String token) {
        return Jwts.parser() // 클레임 조회
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
    //request에서 토큰 가져오는 메서드
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }


}
