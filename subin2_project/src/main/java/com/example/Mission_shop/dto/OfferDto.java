package com.example.Mission_shop.dto;

import com.example.Mission_shop.entity.Offer;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OfferDto {
    private Long id;

    private Integer offerPrice;
    private String status; // 구매 제안 상태 : 제안, 수락, 거절, 확정
    private Long itemId; // Item ID

    public static OfferDto fromEntity(Offer entity) {
        return OfferDto.builder()
                .id(entity.getId())
                .offerPrice(entity.getOfferPrice())
                .status(entity.getStatus())
                .itemId(entity.getItem().getId()) // Item ID 설정
                .build();
    }

}

