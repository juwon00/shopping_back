package com.shopping2.heart;

import com.shopping2.status.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/heart")
public class HeartController {

    private final HeartService heartService;

    @PostMapping
    public ResponseEntity<Message> heart(@RequestHeader("Authorization") String jwt, @RequestBody HeartDto heartDto) {

        heartService.heart(heartDto, jwt);
        return Message.MessagetoResponseEntity(heartDto.getItemName() + " +1 좋아요");
    }

    @DeleteMapping
    public ResponseEntity<Message> unHeart(@RequestHeader("Authorization") String jwt, @RequestBody HeartDto heartDto) {

        heartService.unHeart(heartDto, jwt);
        return Message.MessagetoResponseEntity(heartDto.getItemName() + " 좋아요가 취소되었습니다.");
    }
}
