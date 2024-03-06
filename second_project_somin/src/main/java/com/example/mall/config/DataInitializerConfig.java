package com.example.mall.config;

import com.example.mall.entity.UserEntity;
import com.example.mall.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializerConfig {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            // 관리자 계정이 이미 있는지 확인
            if (!userRepository.existsByUsername("admin")) {
                // 관리자 계정이 없으면 새로 생성
                UserEntity admin = UserEntity.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin")) // 비밀번호 암호화
                        .role("ADMIN")
                        .build();
                userRepository.save(admin);
            }
        };
    }
}
