package com.shopping2.item.dto;

import lombok.Data;

@Data
public class ItemSaveResponse {
    private String name;
    private int price;
    private String category;
    private String url;

    public ItemSaveResponse(String name, int price, String category, String url) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.url = url;
    }
}
