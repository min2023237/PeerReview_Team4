package com.example.Mission_shop.entity;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// UserEntity를 바탕으로 Spring Security 내부에서
// 사용자 정보를 주고받기 위한 객체임을 나타내는 interface UserDetails
// 의 커스텀 구현체
// UserEntity의 dto라고 생각해도 됨
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private Long id;
    @Setter
    private String username;
    @Setter
    private String password;
    @Setter
    private String nickname;
    @Setter
    private String name;
    @Setter
    private Integer age;
    @Setter
    private String email;
    @Setter
    private String phone;

    // 권한 데이터를 담기 위한 속성
    @Setter
    private String authorities;

    // 사업자 등록번호
    @Setter
    private String businessNumber;

    // 사업자 전환 신청
    @Setter
    private String apply;

/*    public String getRawAuthorities() {
        return this.authorities;
    }*/

    @Setter
    private String avatar; // 이미지


    @Override
    // ROLE_USER, ROLE_ADMIN, READ_AUTHORITY, WRITE_AUTHORITY
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities
                = new ArrayList<>();
        String[] rawAuthorities = authorities.split(",");
        for (String rawAuthority : rawAuthorities){
            grantedAuthorities.add(new SimpleGrantedAuthority(rawAuthority));
        }
        return grantedAuthorities;
    }

    // UserEntity를 CustomerUserDetails로 변환하는 정적 메서드
    public static CustomUserDetails fromEntity(UserEntity userEntity) {
        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUsername(userEntity.getUsername());
        userDetails.setPassword(userEntity.getPassword());
        userDetails.setNickname(userEntity.getNickname());
        userDetails.setName(userEntity.getName());
        userDetails.setAge(userEntity.getAge());
        userDetails.setEmail(userEntity.getEmail());
        userDetails.setPhone(userEntity.getPhone());
        userDetails.setAvatar(userEntity.getAvatar());
        /*// 권한은 일단 빈 문자열로 설정
        userDetails.setAuthorities("");*/
        return userDetails;
    }


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
