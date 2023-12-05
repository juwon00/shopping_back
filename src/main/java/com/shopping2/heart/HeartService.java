package com.shopping2.heart;

import com.shopping2.auth.token.JwtService;
import com.shopping2.item.Item;
import com.shopping2.item.repository.ItemRepository;
import com.shopping2.user.UserRepository;
import com.shopping2.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HeartService {

    private final JwtService jwtService;
    private final HeartRepository heartRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public void heart(HeartDto heartDto) {

        String userId = jwtService.extractUserName(heartDto.getUserJwt());
        User user = userRepository.findByLoginId(userId).orElseThrow(NoSuchElementException::new);
        Item item = itemRepository.findByName(heartDto.getItemName()).orElseThrow(NoSuchElementException::new);

        if (heartRepository.existsByUserAndItem(user, item)) {
            throw new IllegalStateException("이미 좋아요를 누른 상태 입니다.");
        }

        Heart createHeart = new Heart(user, item);
        heartRepository.save(createHeart);

        item.increaseHeartsNum();
        itemRepository.save(item);
    }

    public void unHeart(HeartDto heartDto) {

        String userId = jwtService.extractUserName(heartDto.getUserJwt());
        User user = userRepository.findByLoginId(userId).orElseThrow(NoSuchElementException::new);
        Item item = itemRepository.findByName(heartDto.getItemName()).orElseThrow(NoSuchElementException::new);

        Optional<Heart> heart = heartRepository.findHeartByUserAndItem(user, item);

        if (heart.isEmpty()) {
            throw new IllegalStateException("좋아요를 안 누른 상태 입니다.");
        }

        heartRepository.delete(heart.get());

        item.decreaseHeartsNum();
        itemRepository.save(item);
    }

}