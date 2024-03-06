package com.example.Mission_shop.config;

import com.example.Mission_shop.jwt.JwtTokenFilter;
import com.example.Mission_shop.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.access.intercept.AuthorizationFilter;


// @Bean을 비록해서 여러 설정을 하기 위한 Bean 객체
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager manager;

    // 메서드의 결과를 Bean 객체로 관리해주는 어노테이션
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http
                // csrf  보안 헤제
                .csrf(AbstractHttpConfigurer::disable)
                // url에 따른 요청 인가
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/token/issue"
                        )
                        .permitAll()

                        .requestMatchers(
                                "/users/register"
                        )
                        .anonymous()

                        .requestMatchers(
                                "/users/update",
                                "/users/business"
                        )
                        .authenticated()

                        .requestMatchers(
                                "/items/register",
                                "/items/update",
                                "/items/delete",
                                "/items/offer/accept-refuse",
                                "/items/offer/confirm",
                                // 구매 요청 취소
                                "/shop/items/buyRequest/cancel"
                        )
                        .hasRole("USER")

                        .requestMatchers(
                                "/items/buyRequest",
                                "/items/offer/read",
                                // 쇼핑몰 아이템 구매 요청
                                "/shop/items/buyRequest",
                                "/shop/items/buyRequest/sendMoney"
                        )
                        .hasAnyRole("USER","BUSINESS")

                        .requestMatchers(
                                "/items/itemAllList",
                                "/shops/search",
                                "/shop/items/search"
                        )
                        .hasAnyRole("USER" , "BUSINESS", "ADMIN")


                        // 쇼핑몰
                        .requestMatchers(
                                "/shops/update",
                                "/shops/apply/open",
                                "/shops/apply/close",
                                // 쇼핑몰 상품
                                "/shop/items/register",
                                "/shop/items/update",
                                "/shop/items/delete",
                                // 구매 요청 수락
                                "/shop/items/buyRequest/check"
                        )
                        .hasRole("BUSINESS")


                        .requestMatchers(
                                "/users/admin/applyList",
                                "/users/admin/apply/accept-refuse",
                                "/shops/apply/open/acceptRefuse",
                                "/shops/apply/close/read",
                                "/shops/apply/close/acceptRefuse"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(
                                "/shops/apply/read"
                        )
                        .hasAnyRole("BUSINESS","ADMIN")
                )
                // JWT를 사용하기 때문에 보안 관련 세션 헤제
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // JWT 필터를 권한 필터 앞에 삽입
                .addFilterBefore(
                        new JwtTokenFilter(
                                jwtTokenUtils,
                                manager
                        ),
                        AuthorizationFilter.class
                );
        return http.build();
    }

    /*// 사용자 정보 관리 클래스
    public UserDetailsManager userDetailsManager(
            PasswordEncoder passwordEncoder
    ) {
        // 사용자 1
        UserDetails user1 = User.withUsername("user1")
                .password(passwordEncoder.encode("password1"))
                .build();
        // Spring Security에서 기본으로 제공하는,
        // 메모리 기반 사용자 관리 클래스 + 사용자 1
        return new InMemoryUserDetailsManager(user1);
    }*/
}
