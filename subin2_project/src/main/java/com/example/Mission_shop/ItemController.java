package com.example.Mission_shop;

import com.example.Mission_shop.dto.ItemDto;
import com.example.Mission_shop.dto.OfferDto;
import com.example.Mission_shop.exception.AuthenticationFailedException;
import com.example.Mission_shop.exception.ItemNotFoundException;
import com.example.Mission_shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    // 현재 인증된 사용자 정보를 가져오는 메서드
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        } else {
            throw new AuthenticationFailedException("인증 실패: 사용자 이름이나 비밀번호가 잘못되었습니다.");
        }
    }

    // 중고 거래 상품 등록
    @PostMapping("/register")
    public String registerItem(@RequestBody ItemDto itemDto) {
        String username = getCurrentUsername();
        return itemService.registerItem(itemDto, username);
    }

    // 등록된 물품 정보 보기
    @GetMapping("/itemAllList")
    public List<ItemDto> ItemAllList() {
        return itemService.itemAllList();
    }

    // 중고거래 상품 업데이트
    @PostMapping("/update")
    public String updateItem(
            @RequestParam String title,
            @RequestBody ItemDto itemDto
    ){
        String username = getCurrentUsername();
        return itemService.updateItem(itemDto, username, title);
    }

    // 중고거래 상품 삭제
    @PostMapping("/delete")
    public String deleteItem (@RequestParam String title){
        String username = getCurrentUsername();
        return itemService.deleteItem(title, username);
    }

    // 중고거래 상품 거래 제안 :
    @PostMapping("/buyRequest")
    public String buyRequestItem(
            @RequestParam String title,
            @RequestBody OfferDto offerDto
    ) {
        String username = getCurrentUsername();
        return itemService.buyRequestItem(title, offerDto, username);
    }

    // 중고거래 제안 읽기
    @GetMapping("/offer/read")
    public List<OfferDto> readOffer(
            @RequestParam Long id
    ) throws ItemNotFoundException {
        String username = getCurrentUsername();
        return itemService.readOffer(id, username);
    }

    // 중고거래 제안 수락/거절
    @PostMapping("/offer/accept-refuse")
    public String offerAcceptRefuse(
            @RequestParam Long itemId,
            @RequestParam Long offerId,
            @RequestParam String acceptRefuse
    ) {
        String username = getCurrentUsername();
        return itemService.offerAcceptRefuse(itemId, offerId, username, acceptRefuse);
    }

    // 중고거래 제안 확인
    @PostMapping("/offer/confirm")
    public String offerConfirm(
            @RequestParam Long itemId,
            @RequestParam Long offerId
    ) {
        String username = getCurrentUsername();
        return itemService.offerConfirm(itemId, offerId, username);
    }


}

