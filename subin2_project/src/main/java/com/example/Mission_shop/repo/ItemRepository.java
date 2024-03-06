package com.example.Mission_shop.repo;

import com.example.Mission_shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByTitle(String title);
}
