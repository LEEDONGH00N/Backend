package com.example.arom1.repository;

import com.example.arom1.entity.security.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Ref;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByMemberId(Long id);
    Optional<RefreshToken> findByMemberId(Long id);
}
