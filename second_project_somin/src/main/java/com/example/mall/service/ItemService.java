package com.example.mall.service;

import com.example.mall.dto.ItemDto;
import com.example.mall.entity.ItemEntity;
import com.example.mall.entity.UserEntity;
import com.example.mall.repo.ItemRepository;
import com.example.mall.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    // Authentication 객체를 사용하기 위한 AuthenticationManager를 주입받아야 하나,
    // 여기서는 SecurityContextHolder를 통해 직접 접근합니다.

    public ItemService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public UserEntity getCurrentAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public ItemEntity registerItem(ItemDto itemDto) {
        UserEntity user = getCurrentAuthenticatedUser();
        ItemEntity itemEntity = ItemEntity.builder()
                .title(itemDto.getTitle())
                .description(itemDto.getDescription())
                .imageUrl(itemDto.getImageUrl())
                .minimumPrice(itemDto.getMinimumPrice())
                .status("FOR_SALE")
                .user(user)
                .build();

        return itemRepository.save(itemEntity);
    }

    @Transactional
    public ItemEntity updateItem(Long itemId, ItemDto itemDto) {
        UserEntity user = getCurrentAuthenticatedUser();
        ItemEntity itemEntity = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found"));
        if (!itemEntity.getUser().equals(user)) {
            throw new IllegalStateException("Only the item owner can update the item");
        }

        if(itemDto.getTitle() != null) {
            itemEntity.setTitle(itemDto.getTitle());}
        if(itemDto.getDescription() != null) {
            itemEntity.setDescription(itemDto.getDescription());}
        if(itemDto.getImageUrl() != null) {
            itemEntity.setImageUrl(itemDto.getImageUrl());}
        if(itemDto.getMinimumPrice() != null) {
            itemEntity.setMinimumPrice(itemDto.getMinimumPrice());}

        return itemRepository.save(itemEntity);
    }

    @Transactional
    public void deleteItem(Long itemId) {
        UserEntity user = getCurrentAuthenticatedUser();
        ItemEntity itemEntity = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found"));
        if (!itemEntity.getUser().equals(user)) {
            throw new IllegalStateException("Only the item owner can delete the item");
        }

        itemRepository.deleteById(itemId);
    }

    public List<ItemEntity> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<ItemEntity> findById(Long itemId) {
        return itemRepository.findById(itemId);
    }
}
