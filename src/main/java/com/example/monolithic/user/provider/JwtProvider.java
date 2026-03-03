package com.example.monolithic.user.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    // 토큰 만료 시간 (ms)
    private final long ACCESS_TOKEN_EXPIRY  = 1000L * 60 * 30;          // 30분
    private final long REFRESH_TOKEN_EXPIRY = 1000L * 60 * 60 * 24 * 7; // 7일

    // 토큰에 서명
    private Key getStringKey() {
        log.debug(">>> Provider jwt secret: {}", secret);
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // access token provider
    public String createAt(String email) {
        log.debug(">>> Provider createAT: {}", email);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY))
                .signWith(getStringKey())
                .compact();
    }

    // refresh token provider
    public String createRt(String email) {
        log.debug(">>> Provider createRt: {}", email);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY))
                .signWith(getStringKey())
                .compact();
    }

    // token에서 subject(사용자) 추출
    // Spring Security와 연계 시 수정될 예정
    public String getUserEmailFromToken(String token) {
        log.debug(">>> Provider getUserEmailFromToken token: {}", token);

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = Jwts.parser()
                .setSigningKey(getStringKey())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public long getATE() {
        return ACCESS_TOKEN_EXPIRY;
    }

    public long getRTE() {
        return REFRESH_TOKEN_EXPIRY;
    }
}