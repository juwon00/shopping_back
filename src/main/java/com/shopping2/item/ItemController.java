package com.shopping2.item;

import com.shopping2.item.dto.*;
import com.shopping2.item.repository.ItemRepository;
import com.shopping2.item.service.ItemService;
import com.shopping2.status.Message;
import com.shopping2.status.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

    @PostMapping("/new")
    public ResponseEntity<Message> saveItem(@ModelAttribute ItemRegister item) {

        ItemSaveResponse storeItem = itemService.createItem(item);

        Message message = new Message(StatusEnum.OK, "성공 코드", storeItem);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return ResponseEntity.ok().headers(headers).body(message);
    }

    @GetMapping("/items")
    public ResponseEntity<Message> categoryPage(ItemSearchCondition condition, Pageable pageable) {

        Page<ItemDto> result = itemRepository.categoryPage(condition, pageable);

        Message message = new Message(StatusEnum.OK, "성공 코드", result);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return ResponseEntity.ok().headers(headers).body(message);
    }

    @PatchMapping("/modify")
    public ResponseEntity<Message> modifyItem(@ModelAttribute ItemModify item) {

        itemService.modifyItem(item);

        Message message = new Message(StatusEnum.OK, "성공 코드", "수정되었습니다.");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return ResponseEntity.ok().headers(headers).body(message);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Message> deleteItem(@RequestBody ItemDelete item) {

        itemService.deleteItem(item);

        Message message = new Message(StatusEnum.OK, "성공 코드", "삭제되었습니다.");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return ResponseEntity.ok().headers(headers).body(message);
    }
}
