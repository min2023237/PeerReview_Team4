package com.example.mall.service;

import com.example.mall.dto.ProductDto;
import com.example.mall.entity.ProductEntity;
import com.example.mall.entity.ShopEntity;
import com.example.mall.repo.ProductRepository;
import com.example.mall.repo.ShopRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;


    public void registerProduct(
            ProductDto productDto,
            String username) {
        ShopEntity shop = shopRepository.findByOwnerUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found for the user"));
        ProductEntity product = ProductEntity.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .stock(productDto.getStock())
                .imageUrl(productDto.getImageUrl())
                .shop(shop)
                .build();
        productRepository.save(product);

    }

    public void updateProduct(
            Long productId,
            ProductDto productDto,
            String username) {
        ProductEntity product
                = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        if (!product.getShop().getOwner().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not the shop owner");
        }

        if(productDto.getName() != null) {
            product.setName(productDto.getName());}
        if(productDto.getDescription() != null) {
            product.setDescription(productDto.getDescription());}
        if(productDto.getPrice() != null) {
            product.setPrice(productDto.getPrice());}
        if(productDto.getStock() != null) {
            product.setStock(productDto.getStock());}
        if(productDto.getImageUrl() != null) {
            product.setImageUrl(productDto.getImageUrl());}
        productRepository.save(product);
    }

    public void deleteProduct(Long productId, String username) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        if (!product.getShop().getOwner().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not the shop owner");
        }
        productRepository.delete(product);
    }

    public List<ProductEntity> searchByName(
            String productName
    ) {
        return productRepository.findByNameContaining(productName);
    }

    public List<ProductEntity> searchByPriceRange(Double startPrice, Double endPrice) {
        return productRepository.findByPriceBetween(startPrice, endPrice);
    }

    @Transactional
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }
}
