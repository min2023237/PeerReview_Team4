package com.example.mall;

import com.example.mall.dto.ItemDto;
import com.example.mall.entity.CustomUserDetails;
import com.example.mall.entity.ItemEntity;
import com.example.mall.entity.PurchaseProposalEntity;
import com.example.mall.entity.UserEntity;
import com.example.mall.repo.UserRepository;
import com.example.mall.service.ItemService;
import com.example.mall.service.JpaUserDetailsManager;
import com.example.mall.service.PurchaseProposalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final PurchaseProposalService purchaseProposalService;


    @PostMapping("/register")
    public ResponseEntity<String> registerItem(@RequestBody ItemDto itemDto) {
        itemService.registerItem(itemDto);
        return ResponseEntity.ok("상품이 성공적으로 생성되었습니다.");
    }

    @PutMapping("/{item_id}/update")
    public ResponseEntity<String> updateItem(@PathVariable("item_id") Long itemId, @RequestBody ItemDto itemDto) {
        itemService.updateItem(itemId, itemDto);
        return ResponseEntity.ok("상품이 성공적으로 업데이트되었습니다.");
    }

    @DeleteMapping("/{item_id}/delete")
    public ResponseEntity<?> deleteItem(@PathVariable("item_id") Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public String itemList(Model model) {
        List<ItemEntity> items = itemService.getAllItems();
        model.addAttribute("items", items);
        return "itemList"; // itemList.html 뷰 이름
    }

    //item 구매제안
    @PostMapping("/{item_id}/purchase-proposal")
    public ResponseEntity<String> proposePurchase(
            @PathVariable("item_id") Long itemId
            //,Authentication authentication
    ) {
        //CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        //Long userId = userDetails.getId();
        purchaseProposalService.proposePurchase(itemId);
                //, userId);
        return ResponseEntity.ok("구매 제안이 성공적으로 등록되었습니다.");
    }

    @GetMapping("/{item_id}/purchase-proposal/list")
    public String listProposals(
            @PathVariable("item_id") Long itemId,
            @AuthenticationPrincipal UserDetails userDetails,
            //Authentication authentication,
            Model model) {
        //CustomUserDetails userDetails
        //        = (CustomUserDetails) authentication.getPrincipal();
        //Long userId = userDetails.getId(); // 현재 인증된 사용자의 ID


        // 아이템 정보 조회
        //ItemEntity item = itemService.findById(itemId)
        //        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));

        // 아이템 소유자 검증
        //if (!item.getUser().getId().equals(userId)) {
        //    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        //}
        List<PurchaseProposalEntity> proposals =
                purchaseProposalService.listProposals(
                        itemId,
                        userDetails.getUsername());

        model.addAttribute("proposals", proposals);
        model.addAttribute("itemId", itemId);
        return "purchaseProposals"; // Thymeleaf 템플릿 파일 이름
    }


    @PostMapping("/{item_id}/purchase-proposal/{proposal_id}/accept")
    public ResponseEntity<String> acceptProposal(
            @PathVariable("proposal_id") Long proposalId,
            @AuthenticationPrincipal UserDetails userDetails) {
        purchaseProposalService.acceptProposal(proposalId, userDetails.getUsername());
        return ResponseEntity.ok("해당 제안이 수락되었습니다.");
    }

    @PostMapping("/{item_id}/purchase-proposal/{proposal_id}/reject")
    public ResponseEntity<String> rejectProposal(
            @PathVariable("proposal_id") Long proposalId,
            @AuthenticationPrincipal UserDetails userDetails) {
        purchaseProposalService.rejectProposal(proposalId, userDetails.getUsername());
        return ResponseEntity.ok("해당 제안이 거절되었습니다.");
    }

    @PostMapping("/{item_id}/purchase-proposal/{proposal_id}/confirm")
    public ResponseEntity<String> confirmPurchase(
            @PathVariable("proposal_id") Long proposalId,
            @AuthenticationPrincipal UserDetails userDetails) {
        purchaseProposalService.confirmPurchase(proposalId, userDetails.getUsername());
        return ResponseEntity.ok("구매 확정 되었습니다.");
    }

    @GetMapping("/{item_id}/my-purchase-proposal")
    public String myProposals(
            @PathVariable("item_id") Long itemId,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        List<PurchaseProposalEntity> proposals =purchaseProposalService.myProposals(
                itemId, userDetails.getUsername());
        model.addAttribute("proposals", proposals);
        return "myPurchaseProposals";
    }
}
