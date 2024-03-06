package com.example.Mission_shop.repo;

import com.example.Mission_shop.entity.Shop;
import com.example.Mission_shop.entity.ShopItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {
    Optional<ShopItem> findByShopAndName(Shop shop, String name);

    List<ShopItem> findByNameContainingAndPriceBetween(String name, Integer minPrice, Integer maxPrice);

    List<ShopItem> findByNameContaining(String name);

    List<ShopItem> findByPriceBetween(Integer minPrice, Integer maxPrice);

    Optional<ShopItem> findByName(String name);
}
