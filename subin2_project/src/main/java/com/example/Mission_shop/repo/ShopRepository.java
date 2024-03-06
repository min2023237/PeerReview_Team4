package com.example.Mission_shop.repo;

import com.example.Mission_shop.dto.ShopDto;
import com.example.Mission_shop.entity.Shop;
import com.example.Mission_shop.entity.ShopCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByUserUsername(String username);

    List<Shop> findByStatus(String status);

    Optional<Shop> findByName(String name);

    List<Shop> findByUserUsernameAndStatusIn(String username, List<String> list);

    List<Shop> findByClosureRequest(String ClosureRequest);

    List<Shop> findByCategory(ShopCategory category);

    List<Shop> findByNameAndCategory(String name, ShopCategory category);
}
