package com.example.mall.repo;

import com.example.mall.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<ShopEntity, Long> {
    List<ShopEntity> findByStatus(String status);
    Optional<ShopEntity> findByOwnerUsername(String username);
}
