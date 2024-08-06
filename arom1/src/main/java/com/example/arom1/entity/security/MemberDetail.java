package com.example.arom1.entity.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class MemberDetail implements UserDetails {

    private MemberSecurityContext memberSecurityContext;

    public MemberDetail(MemberSecurityContext memberSecurityContext) {
        this.memberSecurityContext = memberSecurityContext;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(memberSecurityContext.getRole()));
    }
    public Long getId() { return memberSecurityContext.getId(); }

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
