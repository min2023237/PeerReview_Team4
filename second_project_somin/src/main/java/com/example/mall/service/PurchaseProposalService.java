package com.example.mall.service;

import com.example.mall.entity.ItemEntity;
import com.example.mall.entity.PurchaseProposalEntity;
import com.example.mall.entity.UserEntity;
import com.example.mall.repo.ItemRepository;
import com.example.mall.repo.PurchaseProposalRepository;
import com.example.mall.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseProposalService {

    private final PurchaseProposalRepository purchaseProposalRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    public UserEntity getCurrentAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
    @Transactional
    public PurchaseProposalEntity proposePurchase(Long itemId) {
//        UserEntity user = userRepository.findById(userId) // userId를 이용하여 UserEntity 조회
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserEntity user = getCurrentAuthenticatedUser();
        PurchaseProposalEntity proposal = PurchaseProposalEntity.builder()
                .itemId(itemId)
                .user(user)
                .status(PurchaseProposalEntity.Status.PROPOSED)
                .build();
        return purchaseProposalRepository.save(proposal);
    }

    public List<PurchaseProposalEntity> listProposals(
            Long itemId,
            String username) {
        //PurchaseProposalEntity purchaseProposal = purchaseProposalRepository.findById(itemId)
        //        .orElseThrow(() -> new IllegalArgumentException("item not found"));
//        if(!purchaseProposal.getUser().getUsername().equals(username)) {
//            throw new SecurityException("You do not have permission to modify this shop");
//        }

        // 아이템 정보 조회
        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("item not found"));

        // 아이템 소유자 검증
        if (!item.getUser().getUsername().equals(username)) {
            throw new SecurityException("You do not have permission to modify this shop");
        }

        return purchaseProposalRepository.findByItemId(itemId);
    }

    @Transactional
    public void acceptProposal(Long proposalId, String username) {
        PurchaseProposalEntity proposal = purchaseProposalRepository.findById(proposalId)
                .orElseThrow(() -> new IllegalArgumentException("Proposal not found"));

        Long itemId = proposal.getItemId();
        // 아이템 정보 조회
        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("item not found"));

        // 아이템 소유자 검증
        if (!item.getUser().getUsername().equals(username)) {
            throw new SecurityException("You do not have permission to modify this shop");
        }

        proposal.setStatus(PurchaseProposalEntity.Status.ACCEPTED);
        purchaseProposalRepository.save(proposal);
    }

    @Transactional
    public void rejectProposal(Long proposalId, String username) {
        PurchaseProposalEntity proposal = purchaseProposalRepository.findById(proposalId)
                .orElseThrow(() -> new IllegalArgumentException("Proposal not found"));

        Long itemId = proposal.getItemId();
        // 아이템 정보 조회
        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("item not found"));

        // 아이템 소유자 검증
        if (!item.getUser().getUsername().equals(username)) {
            throw new SecurityException("You do not have permission to modify this shop");
        }
        proposal.setStatus(PurchaseProposalEntity.Status.REJECTED);
        purchaseProposalRepository.save(proposal);
    }

    @Transactional
    public void confirmPurchase(Long proposalId, String username) {
        PurchaseProposalEntity proposal = purchaseProposalRepository.findById(proposalId)
                .orElseThrow(() -> new IllegalArgumentException("Proposal not found"));

        if(!proposal.getUser().getUsername().equals(username)) {
            throw new SecurityException("You do not have permission to modify this proposal");
        }

        proposal.setStatus(PurchaseProposalEntity.Status.CONFIRMED);
        purchaseProposalRepository.save(proposal);

        Long itemId = proposal.getItemId();
        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        item.setStatus("SOLD");
        itemRepository.save(item);

        List<PurchaseProposalEntity> otherProposals = purchaseProposalRepository.findByItemId(itemId);
        otherProposals.forEach(p -> {
            if (!p.getId().equals(proposalId)) {
                p.setStatus(PurchaseProposalEntity.Status.REJECTED);
            }
        });
        purchaseProposalRepository.saveAll(otherProposals);
    }

    public List<PurchaseProposalEntity> myProposals(Long itemId, String username) {
        UserEntity user = userRepository.findByUsername(username) // userId를 이용하여 UserEntity 조회
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Long userId = user.getId();
        return purchaseProposalRepository.findByUserIdAndItemId(userId, itemId);
    }
}
