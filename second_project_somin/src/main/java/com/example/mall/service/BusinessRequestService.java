package com.example.mall.service;

import com.example.mall.entity.BusinessRequestEntity;
import com.example.mall.entity.ShopEntity;
import com.example.mall.entity.UserEntity;
import com.example.mall.repo.BusinessRequestRepository;
import com.example.mall.repo.ShopRepository;
import com.example.mall.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessRequestService {

    private final UserRepository userRepository;
    private final BusinessRequestRepository businessRequestRepository;
    private final ShopRepository shopRepository;

    public List<BusinessRequestEntity> findPendingRequests() {
        return businessRequestRepository.findByRequestStatus("PENDING");
    }

    public void approveBusinessRequest(Long requestId) {
        BusinessRequestEntity request = businessRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // 사용자의 role을 BUSINESS로 변경
        user.setRole("BUSINESS");
        userRepository.save(user);

        // 사업자 등록 요청의 상태를 APPROVED로 변경
        request.setRequestStatus("APPROVED");
        businessRequestRepository.save(request);

        // ShopEntity 생성 및 저장
        ShopEntity newShop = ShopEntity.builder()
                .name("쇼핑몰 이름을 지어주세요.") // 기본값 또는 요청으로부터 얻은 값을 사용
                .description("쇼핑몰에 대한 설명을 작성해주세요.") // 기본값 또는 요청으로부터 얻은 값을 사용
                .status("PENDING")
                .owner(user)
                .build();
        shopRepository.save(newShop);
    }

    public void rejectBusinessRequest(Long requestId) {
        BusinessRequestEntity request = businessRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

        request.setRequestStatus("REJECTED");
        businessRequestRepository.save(request);
    }
}
