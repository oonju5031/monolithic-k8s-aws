package com.example.monolithic.user.service;

import com.example.monolithic.user.domain.dto.UserRequestDTO;
import com.example.monolithic.user.domain.dto.UserResponseDTO;
import com.example.monolithic.user.domain.entity.UserEntity;
import com.example.monolithic.user.provider.JwtProvider;
import com.example.monolithic.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Qualifier("tokenRedis")  // 여러 RedisTemplate를 사용할 때의 구분자
    private final RedisTemplate<String, Object> redisTemplate;

    private static final long REFRESH_TOKEN_TTL = 60 * 60 * 24 * 7;

    public Map<String, Object> signIn(UserRequestDTO request) {
        log.info(">>>> UserService signIn");
        Map<String, Object> map = new HashMap<>();

        log.debug(">> 1. UserService 사용자 조회");
        UserEntity entity = userRepository
                .findById(request.getEmail())
                .orElseThrow(() -> new RuntimeException(">>> ID Not Found."));

        if (!passwordEncoder.matches(request.getPassword(), entity.getPassword())) {
            throw new RuntimeException("Incorrect Password.");
        }

        log.debug(">> 2. UserService 토큰 생성");
        String at = jwtProvider.createAt(entity.getEmail());
        String rt = jwtProvider.createRt(entity.getEmail());

        log.debug(">> 3. UserService RT토큰을 Redis에 저장");
        log.info(">>> RefreshTokenService saveToken");
        redisTemplate.opsForValue().set("RT: " + entity.getEmail(), rt, REFRESH_TOKEN_TTL);

        map.put("response", UserResponseDTO.fromEntity(entity));
        map.put("access", at);
        map.put("refresh", rt);

        return map;
    }
}
