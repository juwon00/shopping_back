package com.shopping2.heart;

import com.shopping2.auth.token.JwtService;
import com.shopping2.error.CustomException;
import com.shopping2.item.Item;
import com.shopping2.item.repository.ItemRepository;
import com.shopping2.user.UserRepository;
import com.shopping2.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.shopping2.status.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HeartService {

    private final JwtService jwtService;
    private final HeartRepository heartRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public void heart(HeartDto heartDto, String jwt) {

        String userId = jwtService.subStringBearerAndExtractUserLoginId(jwt);
        User user = userRepository.findByLoginId(userId)
                .orElseThrow(() -> new CustomException(NOT_EXIST_HEART_USER));
        Item item = itemRepository.findByName(heartDto.getItemName())
                .orElseThrow(() -> new CustomException(NOT_EXIST_HEART_ITEM));

        if (heartRepository.existsByUserAndItem(user, item)) {
            throw new CustomException(ALREADY_HEART);
        }

        Heart createHeart = new Heart(user, item);
        heartRepository.save(createHeart);

        item.increaseHeartsNum();
        itemRepository.save(item);

        log.info(user.getName() + "님이 " + item.getName() + "에 +1 좋아요");
    }

    public void unHeart(HeartDto heartDto, String jwt) {

        String userId = jwtService.subStringBearerAndExtractUserLoginId(jwt);
        User user = userRepository.findByLoginId(userId)
                .orElseThrow(() -> new CustomException(NOT_EXIST_HEART_USER));
        Item item = itemRepository.findByName(heartDto.getItemName())
                .orElseThrow(() -> new CustomException(NOT_EXIST_HEART_ITEM));

        Optional<Heart> heart = heartRepository.findHeartByUserAndItem(user, item);

        if (heart.isEmpty()) {
            throw new CustomException(ALREADY_UNHEART);
        }

        heartRepository.delete(heart.get());

        item.decreaseHeartsNum();
        itemRepository.save(item);

        log.info(user.getName() + "님이 " + item.getName() + "에 좋아요 취소");
    }

}