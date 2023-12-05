package com.shopping2.item.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ItemModify {
    private String previousName;
    private String itemName;
    private int price;
    private String category;
    private MultipartFile mainFile;
}
