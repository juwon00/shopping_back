package com.shopping2.item;

import com.shopping2.item.dto.*;
import com.shopping2.item.repository.ItemRepository;
import com.shopping2.item.service.ItemService;
import com.shopping2.status.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

    @PostMapping("/new")
    public ResponseEntity<Message> saveItem(@ModelAttribute ItemRegister item) {

        ItemSaveResponse storeItem = itemService.createItem(item);
        log.info(storeItem.getName() + " 상품이 등록되었습니다.");
        return Message.MessagetoResponseEntity(storeItem);
    }

    @GetMapping
    public ResponseEntity<Message> categoryPage(ItemSearchCondition condition, Pageable pageable) {

        Page<ItemDto> result = itemRepository.categoryPage(condition, pageable);
        return Message.MessagetoResponseEntity(result);
    }

    @GetMapping("/heart")
    public ResponseEntity<Message> heartItemPage(@RequestHeader("Authorization") String jwt, Pageable pageable) {

        Page<ItemDto> result = itemService.getHeartItemPage(jwt, pageable);
        return Message.MessagetoResponseEntity(result);
    }


    @PatchMapping("/modify")
    public ResponseEntity<Message> modifyItem(@ModelAttribute ItemModify item) {

        itemService.modifyItem(item);
        log.info(item.getItemName() + "상품이 수정되었습니다.");
        return Message.MessagetoResponseEntity("수정되었습니다.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Message> deleteItem(@RequestBody ItemDelete item) {

        itemService.deleteItem(item);
        log.info(item.getItemName() + "상품이 삭제되었습니다.");
        return Message.MessagetoResponseEntity("삭제되었습니다.");
    }
}
