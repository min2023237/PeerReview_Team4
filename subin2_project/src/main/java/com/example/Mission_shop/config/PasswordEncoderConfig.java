package com.example.Mission_shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
// Bean 객체의 순환 참조는 허용되지 않기 때문에, 순환 참조 문제가 발생할 경우 지금처럼 바깥으로 빼주시면 됩니다
public class PasswordEncoderConfig {
    @Bean
    // 비밀번호 암호화 클래스
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
