package com.shopping2.item;

import com.shopping2.category.Category;
import com.shopping2.heart.Heart;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(length = 2000)
    private String url;

    @OneToMany(mappedBy = "item")
    private List<Heart> hearts = new ArrayList<>();

    private int heartsNum;


    public Item(String name, int price, Category category, String url, int heartsNum) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.url = url;
        this.heartsNum = heartsNum;
    }

    public void increaseHeartsNum() {
        this.heartsNum++;
    }

    public void decreaseHeartsNum() {
        this.heartsNum--;
    }

    public void updateItem(String itemName, int price, Category category, String url) {
        this.name = itemName;
        this.price = price;
        this.category = category;
        this.url = url;
    }
}
