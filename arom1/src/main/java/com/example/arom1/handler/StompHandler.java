package com.example.arom1.handler;


import com.example.arom1.common.util.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {
    private final TokenProvider tokenProvider;

    //websocket을 통해 들어온 요청이 처리되기 전 실행됨
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(StompCommand.CONNECT == accessor.getCommand()) {
            String authorization = tokenProvider.extractJwt(accessor.getFirstNativeHeader("Authorization"));
            tokenProvider.validateToken(authorization);
         }
        return message;
    }


}
