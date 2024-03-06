package com.example.mall;

import com.example.mall.dto.UserUpdateRequestDto;
import com.example.mall.entity.BusinessRequestEntity;
import com.example.mall.entity.CustomUserDetails;
import com.example.mall.entity.UserEntity;
import com.example.mall.jwt.JwtRequestDto;
import com.example.mall.jwt.JwtResponseDto;
import com.example.mall.jwt.JwtTokenUtils;
import com.example.mall.repo.BusinessRequestRepository;
import com.example.mall.repo.UserRepository;
import com.example.mall.service.JpaUserDetailsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final BusinessRequestRepository businessRequestRepository;
    private final JwtTokenUtils jwtTokenUtils;

    @GetMapping("/login")
    public String loginForm() {
        return "login-form";
    }


    // 회원가입 화면
    @GetMapping("/register")
    public String signUpForm() {
        return "register-form";
    }

    @PostMapping("/register")
    public String signUpRequest(
            @RequestParam("username")
            String username,
            @RequestParam("password")
            String password,
            @RequestParam("password-check")
            String passwordCheck
    ) {
        if (password.equals(passwordCheck))
            manager.createUser(CustomUserDetails.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .build());

        // 회원가입 성공 후 로그인 페이지로
        return "redirect:/users/login";
    }

    @PutMapping("/update") // 또는 @PostMapping("/update")
    public ResponseEntity<?> updateUser(
            @RequestBody UserUpdateRequestDto updateRequestDto,
            @RequestHeader("Authorization") String token) {
        String username = getUsernameFromToken(token);
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 사용자 정보 업데이트
        userEntity.setName(updateRequestDto.getName());
        userEntity.setNickname(updateRequestDto.getNickname());
        userEntity.setEmail(updateRequestDto.getEmail());
        userEntity.setPhoneNumber(updateRequestDto.getPhoneNumber());
        userEntity.setAgeGroup(updateRequestDto.getAgeGroup());
        userEntity.setRole("GENERAL"); // 역할을 GENERAL로 설정

        userRepository.save(userEntity);

        return ResponseEntity.ok().build();
    }

    private String getUsernameFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtTokenUtils.parseClaims(token).getSubject();
    }

    @PostMapping("/general/updateBusinessNumber")
    public ResponseEntity<?> updateBusinessNumber(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Map<String, String> request) {
        String businessNumber = request.get("businessNumber");
        if (businessNumber == null) {
            return ResponseEntity.badRequest().body("Business number is required");
        }

        // loadUserByUsername를 사용하여 CustomUserDetails 인스턴스를 얻습니다.
        CustomUserDetails customUserDetails = (CustomUserDetails) manager.loadUserByUsername(userDetails.getUsername());

        // 사용자 정보 업데이트
        UserEntity userEntity = userRepository.findByUsername(customUserDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userEntity.setBusinessNumber(businessNumber);
        //userEntity.setRole("BUSINESS");
        userRepository.save(userEntity);

        // business_requests 테이블에 등록
        BusinessRequestEntity businessNumberRequest = BusinessRequestEntity.builder()
                .username(userDetails.getUsername())
                .businessNumber(businessNumber)
                .requestStatus("PENDING")
                .build();
        businessRequestRepository.save(businessNumberRequest);

        return ResponseEntity.ok().body("User business number updated successfully");
    }


}
