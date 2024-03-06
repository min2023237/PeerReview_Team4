package com.example.Mission_shop.repo;

import com.example.Mission_shop.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByUserId(Long id);

    List<Offer> findByItemId(Long id);

    List<Offer> findByItemIdAndUserId(Long id, Long id1);

    List<Offer> findByItemIdAndIdNot(Long itemId, Long offerId);
}
