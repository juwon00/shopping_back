package com.shopping2.heart;

import com.shopping2.status.Message;
import com.shopping2.status.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/heart")
public class HeartController {

    private final HeartService heartService;

    @PostMapping
    public ResponseEntity<Message> heart(@RequestHeader("Authorization") String jwt, @RequestBody HeartDto heartDto) {

        heartService.heart(heartDto, jwt);

        Message message = new Message(StatusEnum.OK, "성공 코드", heartDto.getItemName() + " +1 좋아요");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return ResponseEntity.ok()
                .headers(headers)
                .body(message);
    }

    @DeleteMapping
    public ResponseEntity<Message> unHeart(@RequestHeader("Authorization") String jwt, @RequestBody HeartDto heartDto) {

        heartService.unHeart(heartDto, jwt);

        Message message = new Message(StatusEnum.OK, "성공 코드", heartDto.getItemName() + " 좋아요가 취소되었습니다.");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return ResponseEntity.ok()
                .headers(headers)
                .body(message);
    }
}
