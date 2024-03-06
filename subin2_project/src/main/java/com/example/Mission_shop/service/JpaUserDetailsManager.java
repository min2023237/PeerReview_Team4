package com.example.Mission_shop.service;

import com.example.Mission_shop.entity.CustomUserDetails;
import com.example.Mission_shop.entity.UserEntity;
import com.example.Mission_shop.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
@Service
public class JpaUserDetailsManager implements UserDetailsManager {
    private final UserRepository userRepository;

    public JpaUserDetailsManager (
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;

        // 이미 관리자 계정이 존재하는지 확인
        if (!userExists("admin")) {
            // 관리자 계정 생성
            createUser(CustomUserDetails.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .authorities("ROLE_ADMIN")
                    .build());
        }
    }

    @Override
    // Spring Security에서 인증을 처리할 때 사용하는 메서드
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser
                = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(username);

        UserEntity userEntity = optionalUser.get();
        return CustomUserDetails.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .authorities(userEntity.getAuthorities())
                .build();
    }

    @Override
    // 편의를 위해 같은 규약으로 회원가입을 하는 메서드
    public void createUser(UserDetails user) {
        if (userExists(user.getUsername())) // 이미 같은 이름의 사용자가 있다는 뜻 -> 오류
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        try{
            CustomUserDetails userDetails =
                    (CustomUserDetails) user;
            UserEntity newUser = UserEntity.builder()
                    .username(userDetails.getUsername())
                    .password(userDetails.getPassword())
                    .email(userDetails.getEmail())
                    .phone(userDetails.getPhone())
                    .authorities("ROLE_INACTIVE") // 회원가입만 할 경우 비활성 사용자
                    .build();

            // 사용자가 "admin"일 경우 권한을 "ROLE_ADMIN"으로 설정
            if (newUser.getUsername().equals("admin")) {
                newUser.setAuthorities("ROLE_ADMIN");
            }

            userRepository.save(newUser);
        }catch (ClassCastException e){
            log.error("Failed Cast to: {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void updateUser(UserDetails user) {
        // 수정하려는 사용자 확인
        if (!userExists(user.getUsername())) {
            throw new UsernameNotFoundException(user.getUsername());
        }

        // 사용자 정보 업데이트
        try {
            CustomUserDetails userDetails = (CustomUserDetails) user;
            UserEntity userEntity = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(user.getUsername()));

            // 업데이트할 사용자 정보 추출
            String nickname = userDetails.getNickname();
            String name = userDetails.getName();
            Integer age = userDetails.getAge();
            String email = userDetails.getEmail();
            String phone = userDetails.getPhone();

            // 엔티티에 새로운 정보 반영
            userEntity.setNickname(nickname);
            userEntity.setName(name);
            userEntity.setAge(age);
            userEntity.setEmail(email);
            userEntity.setPhone(phone);

           /* // 이미지 경로 저장
            userEntity.setAvatar(userDetails.getAvatar());*/

            // 사용자의 권한을 ROLE_USER로 변경
            userEntity.setAuthorities("ROLE_USER");

            // 이미 사업자 전환 신청이 수락된 사용자가 수정할 경우 권한 유지
            if (userEntity.getApply() != null  && userEntity.getApply().equals("ACCEPT")){
                userEntity.setAuthorities("ROLE_BUSINESS");
            }

            // 엔티티 저장
            userRepository.save(userEntity);
        } catch (ClassCastException e) {
            log.error("Failed Cast to: {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 이미지 저장
    /*private void saveImage(String username, MultipartFile image) {
        // 유저 확인
        Optional<UserEntity> optionalUser
                = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        // 파일 저장 위치
        // media/{username}/profile.{확장자}
        // 없다면 폴더 생성
        String profileDir = String.format("media/%s/", username);
        log.info(profileDir); //  확인
        // 주어진 Path를 기준으로, 없는 모든 디렉토리 생성하는 메서드
        try {
            Files.createDirectories(Path.of(profileDir));
        }catch (IOException e) {
            // 폴더를 만드는데 실패하면 기록하고 사용자에게 알림
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 실제 파일 이름을 경로와 확장자를 포함하여 만들기
        String originalFilename = image.getOriginalFilename();
        String[] fileNameSplit = originalFilename.split("\\.");
        String extension = fileNameSplit[fileNameSplit.length - 1];
        String profileFilename = "profile." + extension;
        log.info(profileFilename);

        String profilePath = profileDir + profileFilename;
        log.info(profilePath);

        // 실제로 해당 위치에 파일을 저장
        try {
            image.transferTo(Path.of(profilePath));
        }catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // User에 아바타 위치를 저장
        String requestPath = String.format("/static/%s/%s", username, profileFilename);
        log.info(requestPath);
        UserEntity target = optionalUser.get();
        target.setAvatar(requestPath);

        // 응답하기
        userRepository.save(target);
    }*/

    public void BusinessUser(UserDetails user) {
        // 수정하려는 사용자 확인
        if (!userExists(user.getUsername())) {
            throw new UsernameNotFoundException(user.getUsername());
        }

        // 사용자 정보 업데이트
        try {
            CustomUserDetails userDetails = (CustomUserDetails) user;
            UserEntity userEntity = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(user.getUsername()));

            String businessNumber = userDetails.getBusinessNumber();
            String apply = userDetails.getApply();
            userEntity.setBusinessNumber(businessNumber);
            userEntity.setApply(apply);

            // userEntity.setAuthorities("ROLE_BUSINESS"); // 승인되면
            userRepository.save(userEntity);
        }catch (ClassCastException e) {
            log.error("Failed Cast to: {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }


    // 나중에 구현
    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }
}
