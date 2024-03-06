package com.example.mall.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShopDto {
    private Long id;
    private String name;
    private String description;
    private String status;
    private String reasonForRejection;
    private String reasonForClosure;
    private String category;
    private Long ownerId; // ShopEntity의 owner 필드를 대체하는 사용자 ID

}
