package com.example.mall.service;

import com.example.mall.dto.ShopCategory;
import com.example.mall.dto.ShopListDto;
import com.example.mall.entity.ShopEntity;
import com.example.mall.entity.UserEntity;
import com.example.mall.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.mall.repo.ShopRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    @Transactional
    public void updateShopDetails(
            Long shopId,
            String username,
            String name,
            String description,
            String category) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Shop not found"));

        if (!shop.getOwner().getUsername().equals(username)) {
            throw new SecurityException("You do not have permission to modify this shop");
        }

        // 카테고리 유효성 검사
        try {
            ShopCategory shopCategory = ShopCategory.valueOf(category.toUpperCase());
            shop.setCategory(shopCategory.name());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category");
        }

        shop.setName(name);
        shop.setDescription(description);
        shopRepository.save(shop);
    }

    public void requestOpenShop(Long shopId) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found"));

        // 쇼핑몰 이름, 소개, 분류가 전부 작성되어 있는지 확인
        if (shop.getName() == null || shop.getName().isEmpty() ||
                shop.getDescription() == null || shop.getDescription().isEmpty() ||
                shop.getCategory() == null || shop.getCategory().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop name, description, and category must be provided");
        }

        // 모든 조건을 만족하면 상태를 OPEN_REQUEST로 변경
        shop.setStatus("OPEN_REQUEST");
        shopRepository.save(shop);
    }

    public void requestCloseShop(Long shopId, String reasonForClosure) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found"));

        // 상태가 OPEN일 경우에만 상태를 CLOSE_REQUEST로 변경
        if (!"OPEN".equals(shop.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop is not in an OPEN state");
        }

        shop.setStatus("CLOSE_REQUEST");
        shop.setReasonForClosure(reasonForClosure); // 폐쇄 사유 업데이트
        shopRepository.save(shop);
    }

    public List<ShopEntity> findOpenAndCloseRequests() {
        return shopRepository.findAll().stream()
                .filter(shop -> "OPEN_REQUEST".equals(shop.getStatus()) || "CLOSE_REQUEST".equals(shop.getStatus()))
                .collect(Collectors.toList());
    }

    public void approveOpenRequest(Long shopId) {
        ShopEntity shop = findShopById(shopId);
        shop.setStatus("OPEN");
        shopRepository.save(shop);
    }

    public void rejectOpenRequest(Long shopId, String reasonForRejection) {
        ShopEntity shop = findShopById(shopId);
        shop.setStatus("REJECTED");
        shop.setReasonForRejection(reasonForRejection);
        shopRepository.save(shop);
    }

    public void approveCloseRequest(Long shopId) {
        ShopEntity shop = findShopById(shopId);
        shop.setStatus("CLOSED");
        shopRepository.save(shop);
    }

    private ShopEntity findShopById(Long shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found"));
    }


    public List<ShopListDto> findOpenShops() {
        List<ShopEntity> shops = shopRepository.findByStatus("OPEN");
        return shops.stream().map(shop -> {
            UserEntity owner = userRepository.findById(shop.getOwner().getId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            return new ShopListDto(shop.getName(), shop.getDescription(), shop.getCategory(), owner.getUsername());
        }).collect(Collectors.toList());
    }


}
