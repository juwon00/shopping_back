package com.shopping2.item.repository;

import com.shopping2.category.Category;
import com.shopping2.item.Item;
import com.shopping2.item.repository.querydsl.ItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    boolean existsByName(String name);

    Optional<Item> findByName(String name);

    boolean existsByCategory(Category category);
}
