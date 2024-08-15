package com.example.arom1.handler;

import com.example.arom1.common.exception.BaseException;
import com.example.arom1.common.util.jwt.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ChatPreHandler implements ChannelInterceptor {
    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // ! 연결 요청시 JWT 검증
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // ! Authorization 헤더 추출
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                try {
                    // ! JWT 토큰 검증
                    tokenProvider.validateToken(token);
                    // 토큰에서 memberId 추출
                    Claims claims = Jwts.parser()
                            .parseClaimsJws(token)
                            .getBody();

                    Long memberId = Long.parseLong(claims.get("memberId").toString());

                    // WebSocket 연결 사용자 설정
                    accessor.setUser(new Principal() {
                        @Override
                        public String getName() {
                            return memberId.toString();
                        }
                    });
                } catch (BaseException e) {
                    // JWT 토큰 검증 실패 시 연결 거부
                    return null;
                }
            } else {
                // 토큰이 없거나 형식이 잘못된 경우 연결 거부
                return null;
            }
        }

        return message;
    }
}
