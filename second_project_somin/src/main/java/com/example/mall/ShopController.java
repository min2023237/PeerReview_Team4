package com.example.mall;

import com.example.mall.dto.*;
import com.example.mall.entity.CustomUserDetails;
import com.example.mall.entity.ProductEntity;
import com.example.mall.entity.PurchaseRequestEntity;
import com.example.mall.entity.ShopEntity;
import com.example.mall.service.ProductService;
import com.example.mall.service.PurchaseRequestService;
import com.example.mall.service.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/shops")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;
    private final ProductService productService;
    private final PurchaseRequestService purchaseRequestService;

    // API 엔드포인트로 동작하여 JSON 응답을 반환하는 메서드
    @ResponseBody
    @PutMapping("/{shopId}/update")
    public String updateShop(@PathVariable Long shopId,
                             @AuthenticationPrincipal UserDetails userDetails,
                             @RequestBody ShopUpdateDto request) {

        shopService.updateShopDetails(
                shopId,
                userDetails.getUsername(),
                request.getName(),
                request.getDescription(),
                request.getCategory());
        return "Shop updated successfully";
    }

    @PostMapping("/{shopId}/open-request")
    public ResponseEntity<String> openShopRequest(
            @PathVariable Long shopId
    ) {
        shopService.requestOpenShop(shopId);
        return ResponseEntity.ok("Shop open request submitted successfully");
    }

    @PostMapping("/{shopId}/close-request")
    public ResponseEntity<String> closeShopRequest(@PathVariable Long shopId,
                                                   @RequestBody ShopCloseRequestDto requestDto) {
        shopService.requestCloseShop(shopId, requestDto.getReasonForClosure());
        return ResponseEntity.ok("Shop close request submitted successfully");
    }

    @GetMapping("/list")
    public String listOpenShops(Model model) {
        model.addAttribute("shops", shopService.findOpenShops());
        return "shops/list"; // shops/list.html 뷰 페이지로 이동
    }

    @ResponseBody
    @PostMapping("/register/product")
    public String registerProduct(
            @RequestBody ProductDto productDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        productService.registerProduct(productDto, userDetails.getUsername());
        return "Product created successfully";
    }

    @ResponseBody
    @PutMapping("/product/{productId}/update")
    public String updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductDto productDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        productService.updateProduct(
                productId,
                productDto,
                userDetails.getUsername());
        return "Product updated successfully";
    }

    @ResponseBody
    @DeleteMapping("/product/{productId}/delete")
    public String deleteProduct(@PathVariable Long productId,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        productService.deleteProduct(productId, userDetails.getUsername());
        return "Product deleted successfully";
    }

    @ResponseBody
    @PostMapping("/product/{product_id}/purchase-request")
    public String requestPurchase(
            @PathVariable("product_id") Long productId,
            @RequestBody PurchaseRequestDto purchaseRequestDto
    ) {
        purchaseRequestService.requestPurchase(productId, purchaseRequestDto);
        return "구매 요청이 정상적으로 등록되었습니다.";
    }

    @GetMapping("/product/{product_id}/my-purchase-request")
    public String myRequests(
            @PathVariable("product_id") Long proudctId,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        List<PurchaseRequestEntity> purchaseRequests = purchaseRequestService.myRequests(
                proudctId, userDetails.getUsername());
        model.addAttribute("purchaseRequests", purchaseRequests);
        return "myRequests";
    }

    @PostMapping("/product/{product_id}/purchase-request/{request_id}/cancel")
    public ResponseEntity<String> cancelRequest(
            @PathVariable("product_id") Long productId,
            @PathVariable("request_id") Long requestId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        purchaseRequestService.cancelRequest(productId, requestId, userDetails.getUsername());
        return ResponseEntity.ok("구매 요청이 취소되었습니다.");
    }

    @GetMapping("/product/{product_id}/purchase-request/list")
    public String listRequests(
            @PathVariable("product_id") Long productId,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        List<PurchaseRequestEntity> requests =
                purchaseRequestService.listRequests(
                        productId,
                        userDetails.getUsername()
                );

        model.addAttribute("requests", requests);
        model.addAttribute("productId", productId);
        return "purchaseRequests";
    }

    @PostMapping("/product/{product_id}/purchase-request/{request_id}/accept")
    public String acceptRequest(
            @PathVariable("product_id") Long productId,
            @PathVariable("request_id") Long requestId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        purchaseRequestService.acceptRequest(productId, requestId, userDetails.getUsername());
        return "해당 요청이 수락되었습니다.";
    }

    @GetMapping("/product/search/{product_name}")
    public String searchByName(
            @PathVariable("product_name")
            String productName,
            Model model
    ) {
        List<ProductEntity> products = productService.searchByName(productName);
        model.addAttribute("products", products);
        return "productList";
    }

    // 가격 범위로 검색 (예시: /product/search/price?start=10&end=100)
    @GetMapping("/product/search/price")
    public String searchByPriceRange(
            Double start,
            Double end,
            Model model) {
        List<ProductEntity> products = productService.searchByPriceRange(start, end);
        model.addAttribute("products", products);
        return "productList";
    }

    @GetMapping("/product/list")
    public String productList(
            Model model
    ) {
        model.addAttribute("products", productService.getAllProducts());
        return "productList";
    }

}
