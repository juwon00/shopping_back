package com.shopping2.heart;

import com.shopping2.item.Item;
import com.shopping2.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    Optional<Heart> findHeartByUserAndItem(User user, Item item);

    boolean existsByUserAndItem(User user, Item item);

    void deleteAllByItem(Item item);

    List<Heart> findAllByUser(User user);
}
