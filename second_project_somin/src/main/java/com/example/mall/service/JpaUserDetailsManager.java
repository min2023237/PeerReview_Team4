package com.example.mall.service;

import com.example.mall.entity.CustomUserDetails;
import com.example.mall.entity.UserEntity;
import com.example.mall.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
//@RequiredArgsConstructor
@Service
public class JpaUserDetailsManager implements UserDetailsManager {

    private final UserRepository userRepository;

    public JpaUserDetailsManager(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;

    }

    @Override
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
                .name(userEntity.getName())
                .nickname(userEntity.getNickname())
                .email(userEntity.getEmail())
                .phoneNumber(userEntity.getPhoneNumber())
                .ageGroup(userEntity.getAgeGroup())
                .role(userEntity.getRole())
                .businessNumber(userEntity.getBusinessNumber())
                .imageUrl(userEntity.getImageUrl())
                .build();

    }

    @Override
    public void createUser(UserDetails user) {
        if (userExists(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        try {
            CustomUserDetails userDetails =
                    (CustomUserDetails) user;
            UserEntity newUser = UserEntity.builder()
                    .username(userDetails.getUsername())
                    .password(userDetails.getPassword())
                    .role("INACTIVE")
                    .build();
            userRepository.save(newUser);
        } catch (ClassCastException e) {
            log.error("Failed Cast to: {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    // 나중에...
    @Override
    public void updateUser(UserDetails user) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public void deleteUser(String username) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }
}
