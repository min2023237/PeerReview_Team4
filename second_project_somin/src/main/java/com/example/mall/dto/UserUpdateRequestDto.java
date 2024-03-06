package com.example.mall.dto;

import lombok.Data;

@Data
public class UserUpdateRequestDto {
    private String name;
    private String nickname;
    private String email;
    private String phoneNumber;
    private String ageGroup;
}
