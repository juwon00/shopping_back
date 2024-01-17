package com.shopping2.item.repository.querydsl;


import com.shopping2.item.dto.ItemDto;
import com.shopping2.item.dto.ItemSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {

    Page<ItemDto> ItemPage(ItemSearchCondition condition, Pageable pageable);

    Page<ItemDto> heartPage(List<Long> userIdList, Pageable pageable);
}
