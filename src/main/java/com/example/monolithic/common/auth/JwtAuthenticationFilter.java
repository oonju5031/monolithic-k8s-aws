package com.example.monolithic.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String secret;
    private Key key;

    @PostConstruct
    private void init() {
        log.info(">>> Provider jwt secret: {}", secret);
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.info(">>> JwtAuthenticationFilter doFilterInternal");

        String endPoint = request.getRequestURI();
        String method = request.getMethod();
        log.info(">>> JwtAuthenticationFilter User EndPoint: {}", request.getRequestURI());
        log.info(">>> JwtAuthenticationFilter Request Method: {}", method);

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        // header, token 검증을 해서
        // 통과
        String authHeader = request.getHeader("Authorization");
        log.info(">>> JwtAuthenticationFilter authHeader: {}", authHeader);
        //  또는 reject
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info(">>> JwtAuthenticationFilter Not Authorized.");
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        log.info(">>> JwtAuthenticationFilter token: {}", token);
        log.info(">> JwtAuthenticationFilter Validation Check");

        try {
            // Claims: JWT 내에 포함된 사용자 정보의 집합 (== JWT 데이터)
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String email = claims.getSubject();
            log.info(">>> JwtAuthenticationFilter claims get email: {}", email);

            // JwtProvider에 의해 Role이 입력된 경우에만 해당
            String role = claims.get("role", String.class);
            log.info(">>> JwtAuthenticationFilter claims get role: {}", role);

            // Spring Security의 인증 정보를 담는 객체: UsernamePasswordAuthenticationToken을 자주 사용
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            role != null ?
                                    java.util.List.of(() -> "ROLE_" + role) :
                                    java.util.List.of());

            // 사용자의 요청과 인증 정보 객체를 연결
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // SecurityContext 저장 -> 컨트롤러에서 필요로 할 때 꺼낼 수 있음
            // 사용자의 상태 정보를 확인
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            log.error("Exception", e);
        }

        chain.doFilter(request, response);
    }
}
