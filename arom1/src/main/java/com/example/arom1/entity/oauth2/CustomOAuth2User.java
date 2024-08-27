package com.example.arom1.entity.oauth2;

import com.example.arom1.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
    Member member;  // 토큰 만들기 위해 Member 객체를 담아서 전달

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            Member member) {
        super(authorities, attributes, nameAttributeKey);
        this.member = member;
    }

}
