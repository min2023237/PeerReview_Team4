package com.example.mall.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    @Getter
    private Long id;
    @Getter
    private String username;
    private String password;
    @Getter
    private String name;
    @Getter
    private String nickname;
    @Getter
    private String email;
    @Getter
    private String phoneNumber;
    @Getter
    private String ageGroup;
    @Getter
    private String role;
    @Getter
    private String businessNumber;
    @Getter
    private String imageUrl;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> grantedAuthorities
                = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.role));
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }




    // 먼 미래
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
