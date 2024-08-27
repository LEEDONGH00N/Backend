package com.example.arom1.entity.security;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "refreshToken")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String refreshToken;
    private Long memberId;

    @Builder
    private RefreshToken(String refreshToken, Long memberId) {
        this.refreshToken = refreshToken;
        this.memberId = memberId;
    }


    public static RefreshToken of(String refreshToken, Long memberId) {
        return RefreshToken.builder()
                .memberId(memberId)
                .refreshToken(refreshToken)
                .build();
    }

}
