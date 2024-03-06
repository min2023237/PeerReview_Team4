package com.example.mall.dto;

import lombok.Data;

@Data
public class PurchaseRequestDto {
    private Double passedPrice;
    private Long quantity;
}
