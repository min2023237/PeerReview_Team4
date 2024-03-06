package com.example.mall.config;

import com.example.mall.jwt.JwtTokenFilter;
import com.example.mall.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;


@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager manager;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authHttp -> authHttp

                                //.authenticated()
                                .requestMatchers(
                                        "/users/login",
                                        "/users/register"
                                )
                                .anonymous()
                                .requestMatchers("/items/**")
                                .hasAnyRole("GENERAL","BUSINESS", "ADMIN")
                                .requestMatchers("/users/general/**")
                                .hasAnyRole("GENERAL","BUSINESS", "ADMIN")
                                .requestMatchers("/shops/**")
                                .hasAnyRole("GENERAL","BUSINESS", "ADMIN")
                                .requestMatchers("/admin/**")
                                .hasRole("ADMIN")

                                // AUTHORITY에 따른 접근 설정
                                //.requestMatchers("/auth/read-authority")
                                //.hasAnyAuthority("READ_AUTHORITY", "WRITE_AUTHORITY")

                                //.requestMatchers("/auth/write-authority")
                                //.hasAuthority("WRITE_AUTHORITY")

                                .anyRequest()
                                .permitAll()
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        new JwtTokenFilter(
                                jwtTokenUtils,
                                manager
                        ),
                        AuthorizationFilter.class
                )
        ;

        return http.build();
    }

}
