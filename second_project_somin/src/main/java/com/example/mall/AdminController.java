package com.example.mall;

import com.example.mall.service.BusinessRequestService;
import com.example.mall.service.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final BusinessRequestService businessRequestService;
    private final ShopService shopService;

    @GetMapping("/business-requests")
    public String listPendingRequests(Model model) {
        model.addAttribute("requests", businessRequestService.findPendingRequests());
        return "businessRequests";
    }

    @PostMapping("/business-requests/{requestId}/approve")
    public ResponseEntity<String> approveRequest(
            @PathVariable Long requestId) {
        businessRequestService.approveBusinessRequest(requestId);
        return ResponseEntity.ok("Business request approved successfully.");
    }

    @PostMapping("/business-requests/{requestId}/reject")
    public ResponseEntity<String> rejectRequest(
            @PathVariable Long requestId) {
        businessRequestService.rejectBusinessRequest(requestId);
        return ResponseEntity.ok("Business request rejected successfully.");
    }

    @GetMapping("/shops/requests")
    public String showShopRequests(Model model) {
        model.addAttribute("shops", shopService.findOpenAndCloseRequests());
        return "shops/requests"; // shops/requests.html 뷰 페이지로 이동
    }

    @PostMapping("/open-requests/{shopId}/approve")
    public ResponseEntity<String> approveOpenRequest(@PathVariable Long shopId) {
        shopService.approveOpenRequest(shopId);
        return ResponseEntity.ok("Shop open request approved");
    }

    @PostMapping("/open-requests/{shopId}/reject")
    public ResponseEntity<String> rejectOpenRequest(
            @PathVariable Long shopId,
            @RequestBody Map<String, String> body) {
        String reasonForRejection = body.get("reasonForRejection");
        shopService.rejectOpenRequest(shopId, reasonForRejection);
        return ResponseEntity.ok("Shop open request rejected");
    }

    @PostMapping("/close-requests/{shopId}/approve")
    public ResponseEntity<String> approveCloseRequest(@PathVariable Long shopId) {
        shopService.approveCloseRequest(shopId);
        return ResponseEntity.ok("Shop close request approved");
    }

}
