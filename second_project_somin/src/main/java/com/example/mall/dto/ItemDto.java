package com.example.mall.dto;

import lombok.Data;

@Data
public class ItemDto {
    private String title;
    private String description;
    private String imageUrl;
    private Double minimumPrice;
    private String status;
}
