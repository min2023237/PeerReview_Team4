    package com.example.Mission_shop.service;

    import com.example.Mission_shop.dto.ItemDto;
    import com.example.Mission_shop.dto.OfferDto;
    import com.example.Mission_shop.entity.Item;
    import com.example.Mission_shop.entity.Offer;
    import com.example.Mission_shop.entity.UserEntity;
    import com.example.Mission_shop.exception.ItemNotFoundException;
    import com.example.Mission_shop.repo.ItemRepository;
    import com.example.Mission_shop.repo.OfferRepository;
    import com.example.Mission_shop.repo.UserRepository;
    import lombok.AllArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.stereotype.Service;

    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.List;
    import java.util.Optional;

    @Slf4j
    @Service
    @AllArgsConstructor
    public class ItemService {
        private final UserRepository userRepository;
        private final ItemRepository itemRepository;
        private final OfferRepository offerRepository;

        public String registerItem(ItemDto itemDto, String username) {
            // 아이템 등록 처리
            Item item = new Item();
            item.setTitle(itemDto.getTitle());
            item.setDescription(itemDto.getDescription());
            item.setMinimumPrice(itemDto.getMinimumPrice());
            item.setStatus("판매중"); // 처음 등록할 때 "판매중" 상태로 설정
            item.setUser(userRepository.findIdByUsername(username).orElse(null)); // 현재 사용자의 아이디 설정

            // 아이템 저장
            itemRepository.save(item);

            return "중거고래 상품을 등록하였습니다.";
        }

        public List<ItemDto> itemAllList() {
            // 모든 물품 정보를 가져와서 리스트로 저장
            List<Item> itemList = itemRepository.findAll();

            // 각 물품을 ItemDto로 변환하여 저장할 리스트 생성
            List<ItemDto> itemDtoList = new ArrayList<>();

            // 각 물품을 ItemDto로 변환하여 리스트에 추가
            for (Item item : itemList) {
                itemDtoList.add(ItemDto.fromEntity(item));
            }

            // 변환된 ItemDto 리스트 반환
            return itemDtoList;
        }

        public String updateItem(ItemDto itemDto, String username, String title) {
            // 아이템 내용 수정
            // 아이템 엔티티를 가져옴
            Optional<Item> optionalItem = itemRepository.findByTitle(title);

            if (optionalItem.isPresent()) {
                Item item = optionalItem.get();

                // 현재 사용자가 해당 아이템의 소유자인지 확인
                if (!item.getUser().getUsername().equals(username)) {
                    return "해당 상품을 업데이트할 권한이 없습니다.";
                }

                // 아이템 정보 수정
                item.setTitle(itemDto.getTitle());
                item.setDescription(itemDto.getDescription());
                item.setMinimumPrice(itemDto.getMinimumPrice());

                // 아이템 저장
                itemRepository.save(item);

                return "상품을 업데이트 했습니다.";
            } else {
                return username + "사용자의 " + title + "상품을 찾을 수 없습니다.";
            }
        }

        public String deleteItem(String  title, String username) {
            // 아이템 삭제
            // 아이템 엔티티를 가져옴
            Optional<Item> optionalItem = itemRepository.findByTitle(title);

            if (optionalItem.isPresent()) {
                Item item = optionalItem.get();

                // 현재 사용자가 해당 아이템의 소유자인지 확인
                if (item.getUser().getUsername().equals(username)) {
                    // 아이템 정보 삭제
                    itemRepository.delete(item);
                    return "상품을 삭제했습니다.";
                } else {
                    return "이 상품을 삭제할 권한이 없습니다.";
                }
            } else {
                return title + " 상품을 찾을 수 없습니다.";
            }
        }

        public String buyRequestItem(String title, OfferDto offerDto, String username) {
            // 해당 itemId로 Item을 찾음
            Item item = itemRepository.findByTitle(title)
                    .orElseThrow(() -> new RuntimeException("해당 상품을 찾을 수 없습니다."));

            // 현재 사용자가 해당 아이템의 소유자인지 확인 ,  일치하지 않을 경우 구매 제안
            if (!item.getUser().getUsername().equals(username)) {
                Offer offer = new Offer();
                // 구매 제안 등록 처리
                offer.setItem(item);
                // 구매 제안을 하는 사용자를 설정
                UserEntity user = userRepository.findIdByUsername(username)
                        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
                offer.setUser(user);
                offer.setOfferPrice(offerDto.getOfferPrice());
                offer.setStatus("구매 제안");
                // 구매 제안 저장
                offerRepository.save(offer);

                return "구매 제안이 등록되었습니다.";
            }else{
                return "자신의 상품에는 구매 제안을 할 수 없습니다.";
            }
        }

        // 등록한 사용자와 제안한 사용자만 구매제안 조회, username은 토큰을 입력한 사용자의 usernmae
        public List<OfferDto> readOffer(Long id, String username) throws ItemNotFoundException {
            // id는 구매제안한 item id
            // id로 물품 조회
            Item item = itemRepository.findById(id)
                    .orElseThrow(() -> new ItemNotFoundException("해당 id의 상품을 찾을 수 없습니다. 상품 id:" + id));

            // username으로 user id 가져오기
            UserEntity user = userRepository.findIdByUsername(username)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            // 물품을 등록한 사용자가 요청한 경우
            if (item.getUser().getId().equals(user.getId())) {
                // 물품 등록자인 경우, 해당 물품의 모든 제안 조회
                List<Offer> offers = offerRepository.findByItemId(id);
                if (offers.isEmpty()) {
                    return Collections.emptyList();
                }
                return mapToOfferDtoList(offers);
            } else {
                // 구매 제안 사용자인 경우, 해당 물품에 대한 자신의 제안만 조회
                List<Offer> offers = offerRepository.findByItemIdAndUserId(id, user.getId());
                return mapToOfferDtoList(offers);
            }
        }

        private List<OfferDto> mapToOfferDtoList(List<Offer> offers) {
            List<OfferDto> offerDtos = new ArrayList<>();
            for (Offer offer : offers) {
                OfferDto offerDto = OfferDto.fromEntity(offer);
                offerDtos.add(offerDto);
            }
            return offerDtos;
        }

        public String offerAcceptRefuse(Long itemId, Long offerId, String username, String acceptRefuse) {
            // offer list에서 offerId에 해당하는 항목 조회
            Optional<Offer> optionalOffer = offerRepository.findById(offerId);
            if (optionalOffer.isPresent()) {
                Offer offer = optionalOffer.get();

                // 해당 offer에 대한 물품 조회
                Optional<Item> optionalItem = itemRepository.findById(itemId);
                if (optionalItem.isPresent()) {
                    Item item = optionalItem.get();

                    // 물품을 등록한 사용자와 현재 사용자가 같을 경우
                    if (item.getUser().getUsername().equals(username)) {
                        // acceptRefuse를 offer의 status에 저장
                        offer.setStatus(acceptRefuse);
                        offerRepository.save(offer); // 변경사항 저장
                        return "구매 제안 상태가 업데이트 되었습니다.";
                    }
                }
            }
            return "제안 상태를 업데이트하지 못했습니다. 제안을 찾을 수 없거나 승인되지 않았습니다.";
        }

        // 구매 확정 - 대상 물품의 상태는 판매 완료, 다른 구매 제안의 상태는 거절
        public String offerConfirm(Long itemId, Long offerId, String username) {
            // 해당 offerId에 해당하는 제안 조회
            Optional<Offer> optionalOffer = offerRepository.findById(offerId);
            if (optionalOffer.isPresent()) {
                Offer offer = optionalOffer.get();

                // 현재 사용자가 제안의 소유자인지 확인
                if (offer.getUser().getUsername().equals(username)) {
                    // 제안의 상태가 "수락"인지 확인
                    if (offer.getStatus().equals("수락")) {
                        // 제안의 상태를 "구매 확정"으로 변경
                        offer.setStatus("구매 확정");
                        offerRepository.save(offer); // 변경사항 저장

                        // 해당 물품의 상태를 "판매 완료"로 변경
                        Optional<Item> optionalItem = itemRepository.findById(itemId);
                        if (optionalItem.isPresent()) {
                            Item item = optionalItem.get();
                            item.setStatus("판매 완료");
                            itemRepository.save(item); // 변경사항 저장
                        }

                        // itemId는 같지만 offerId가 다른 구매 제안들의 상태를 "거절"로 변경
                        List<Offer> otherOffers = offerRepository.findByItemIdAndIdNot(itemId, offerId);
                        for (Offer otherOffer : otherOffers) {
                            otherOffer.setStatus("거절");
                            offerRepository.save(otherOffer); // 변경사항 저장
                        }

                        return "제안이 확인되었습니다.";
                    } else {
                        return "제안 확인 실패: 제안 상태가 승인되지 않았습니다.";
                    }
                } else {
                    return "제안 확인 실패: 구매 제안 사용자만 확인할 수 있습니다.";
                }
            } else {
                return "제안 확인 실패: 제안을 찾을 수 없습니다.";
            }
        }
    }

