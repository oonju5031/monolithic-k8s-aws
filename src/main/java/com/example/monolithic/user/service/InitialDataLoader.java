package com.example.monolithic.user.service;

import com.example.monolithic.user.domain.entity.UserEntity;
import com.example.monolithic.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitialDataLoader implements CommandLineRunner {  // 서버 기동 직후 실행되는 컴포넌트

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info(">>> InitialDataLoader run called");

        if (userRepository.findById("admin@naver.com").isPresent()) {
            return;
        } else {
            userRepository.save(UserEntity.builder()
                            .email("admin@naver.com")
                            .name("admin")
                            .password(passwordEncoder.encode("1234"))
                            .role("ADMIN")
                            .build());
        }

    }
}
