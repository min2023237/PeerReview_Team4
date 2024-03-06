package com.example.mall.repo;

import com.example.mall.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    // 필요한 경우 추가적인 메서드를 여기에 정의할 수 있습니다.
    // 상품 이름으로 검색
    List<ProductEntity> findByNameContaining(String name);

    // 가격 범위로 검색
    List<ProductEntity> findByPriceBetween(Double startPrice, Double endPrice);
}

