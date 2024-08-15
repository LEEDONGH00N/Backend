//package com.example.arom1.service;
//
//import com.example.arom1.entity.oauth2.KakaoUserDetails;
//import com.example.arom1.entity.oauth2.OAuth2UserInfo;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
//
//    private final MemberService memberService;
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//
//        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
//        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
//
//        String provider = userRequest
//                .getClientRegistration()
//                .getRegistrationId();
//
//        OAuth2UserInfo oAuth2UserInfo = findProvider(provider, oAuth2User);
//
//        String providerId = oAuth2UserInfo.getProviderId();
//        String email = oAuth2UserInfo.getEmail();
//        String loginId = provider + "_" + providerId;
//
//
//        // 이메일로 가입된 회원인지 조회한다.
//        Optional<Member> findMember = memberService.findByEmail(email);
//
//        if (findMember.isEmpty()) {
//            // 회원이 존재하지 않을경우, memberAttribute의 exist 값을 false로 넣어준다.
//            memberAttribute.put("exist", false);
//            // 회원의 권한(회원이 존재하지 않으므로 기본권한인 ROLE_USER를 넣어준다), 회원속성, 속성이름을 이용해 DefaultOAuth2User 객체를 생성해 반환한다.
//            return new DefaultOAuth2User(
//                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
//                    memberAttribute, "email");
//        }
//
//        // 회원이 존재할경우, memberAttribute의 exist 값을 true로 넣어준다.
//        memberAttribute.put("exist", true);
//        // 회원의 권한과, 회원속성, 속성이름을 이용해 DefaultOAuth2User 객체를 생성해 반환한다.
//        return new DefaultOAuth2User(
//                Collections.singleton(new SimpleGrantedAuthority("ROLE_".concat(findMember.get().getUserRole()))),
//                memberAttribute, "email");
//    }
//
//
//
//    private OAuth2UserInfo findProvider(String provider, OAuth2User oAuth2User) {
//        if(provider.equals("kakao")){
//            System.out.println("카카오 로그인");
//            return new KakaoUserDetails(oAuth2User.getAttributes());
//        }
////        if(provider.equals("google")){
////            System.out.println("구글 로그인");
////            return new GoogleUserDetails(oAuth2User.getAttributes());
////        }
//
//        return null;
//    }
//
//
//}
