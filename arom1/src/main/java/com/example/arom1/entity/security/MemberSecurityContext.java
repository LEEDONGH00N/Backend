package com.example.arom1.entity.security;

import com.example.arom1.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberSecurityContext {
    private Long id;
    private String role;
    private String name;
    private String email;
    private String password;

    @Builder
    public MemberSecurityContext(Long id, String role, String name, String email, String password) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static MemberSecurityContext of(Member member) {
        return MemberSecurityContext.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .password(member.getPassword())
                .role("MEMBER")
                .build();
    }

}
