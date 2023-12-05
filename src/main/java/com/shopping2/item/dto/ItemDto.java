package com.shopping2.item.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDto {

    private String name;
    private int price;
    private String url;
    private int heartsNum;

    @QueryProjection
    public ItemDto(String name, int price, String url, int heartsNum) {
        this.name = name;
        this.price = price;
        this.url = url;
        this.heartsNum = heartsNum;
    }
}
