package com.shopping2.item.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shopping2.item.dto.ItemDto;
import com.shopping2.item.dto.ItemSearchCondition;
import com.shopping2.item.dto.QItemDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.shopping2.item.QItem.item;


public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ItemRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<ItemDto> categoryPage(ItemSearchCondition condition, Pageable pageable) {
        List<ItemDto> content = queryFactory
                .select(new QItemDto(item.name, item.price, item.url, item.heartsNum))
                .from(item)
                .where(categoryEq(condition.getCategoryId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(item.count())
                .from(item)
                .where(categoryEq(condition.getCategoryId()));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<ItemDto> heartPage(List<Long> userIdList, Pageable pageable) {
        List<ItemDto> content = queryFactory
                .select(new QItemDto(item.name, item.price, item.url, item.heartsNum))
                .from(item)
                .where(item.id.in(userIdList))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(item.count())
                .from(item)
                .where(item.id.in(userIdList));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression categoryEq(Long categoryId) {
        return categoryId != null ? item.category.id.eq(categoryId) : null;
    }


}
