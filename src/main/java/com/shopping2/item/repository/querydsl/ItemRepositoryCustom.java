package com.shopping2.item.repository.querydsl;


import com.shopping2.item.dto.ItemDto;
import com.shopping2.item.dto.ItemSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<ItemDto> categoryPage(ItemSearchCondition condition, Pageable pageable);
}
