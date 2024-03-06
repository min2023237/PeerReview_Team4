package com.example.mall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopListDto {
    private String name;
    private String description;
    private String category;
    private String username; // 소유자의 username
}
