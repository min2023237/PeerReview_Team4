package com.example.mall.service;

import com.example.mall.dto.PurchaseRequestDto;
import com.example.mall.entity.ProductEntity;
import com.example.mall.entity.PurchaseRequestEntity;
import com.example.mall.entity.UserEntity;
import com.example.mall.repo.ProductRepository;
import com.example.mall.repo.PurchaseRequestRepository;
import com.example.mall.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseRequestService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PurchaseRequestRepository purchaseRequestRepository;

    public UserEntity getCurrentAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public void requestPurchase(Long productId, PurchaseRequestDto purchaseRequestDto) {
        UserEntity user = getCurrentAuthenticatedUser();
        PurchaseRequestEntity requestEntity = PurchaseRequestEntity.builder()
                .productId(productId)
                .user(user)
                .passedPrice(purchaseRequestDto.getPassedPrice())
                .quantity(purchaseRequestDto.getQuantity())
                .status(PurchaseRequestEntity.Status.REQUESTED)
                .build();
        purchaseRequestRepository.save(requestEntity);
    }

    public List<PurchaseRequestEntity> myRequests(
            Long productId,
            String username
    ) {
        UserEntity user = userRepository.findByUsername(username) // userId를 이용하여 UserEntity 조회
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Long userId = user.getId();
        return purchaseRequestRepository.findByUserIdAndProductId(userId, productId);
    }

    @Transactional
    public void cancelRequest(Long productId, Long requestId, String username) {
        PurchaseRequestEntity purchaseRequest = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("requests not found"));

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("product not found"));
        if(!purchaseRequest.getUser().getUsername().equals(username)) {
            throw  new SecurityException("You do not have permission to modify this request");
        }

        if(!purchaseRequest.getStatus().equals(PurchaseRequestEntity.Status.REQUESTED)) {
            throw new IllegalArgumentException("구매요청을 취소할 수 없습니다.");
        }

        purchaseRequest.setStatus(PurchaseRequestEntity.Status.CANCELED);
        purchaseRequestRepository.save(purchaseRequest);
    }

    public List<PurchaseRequestEntity> listRequests(
            Long productId,
            String username
    ) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("product not found"));

        UserEntity userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!product.getShop().getOwner().equals(userId)) {
            throw new IllegalArgumentException("You do not have permission to see this list");
        }

        return purchaseRequestRepository.findByProductIdAndStatus(productId, PurchaseRequestEntity.Status.REQUESTED);
    }

    @Transactional
    public void acceptRequest(
            Long productId, Long requestId, String username
    ) {
        PurchaseRequestEntity request = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Requests not found"));

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("product not found"));

        UserEntity userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!product.getShop().getOwner().equals(userId)) {
            throw new IllegalArgumentException("You do not have permission to see this list");
        }

        Long currentStock = product.getStock() - request.getQuantity();
        product.setStock(currentStock.intValue());
        request.setStatus(PurchaseRequestEntity.Status.COMPLETED);
        purchaseRequestRepository.save(request);

    }
}
