package com.example.arom1.entity.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MemberDetail implements UserDetails, OAuth2User {

    private final MemberSecurityContext memberSecurityContext;
    private Map<String, Object> attributes;

    public MemberDetail(MemberSecurityContext memberSecurityContext) {
        this.memberSecurityContext = memberSecurityContext;
    }

    // OAuth2로 회원가입, 로그인 하기 위한 생성자
    public MemberDetail(MemberSecurityContext memberSecurityContext, Map<String, Object> attributes) {
        this.memberSecurityContext = memberSecurityContext;
        this.attributes = attributes;
    }

    // OAuth2User 오버라이딩 메서드
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }

    public Long getId() { return memberSecurityContext.getId(); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(memberSecurityContext.getRole()));
    }

    @Override
    public String getUsername() {
        return memberSecurityContext.getEmail();
    }

    @Override
    public String getPassword() {
        return memberSecurityContext.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않음
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // true -> 잠금되지 않음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // true -> 만료되지 않음
    }

    // 계정 사용 가능 여부 변환
    @Override
    public boolean isEnabled() {
        return true; // true -> 사용 가능
    }


}
