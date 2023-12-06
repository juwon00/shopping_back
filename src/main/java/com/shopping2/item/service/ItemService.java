package com.shopping2.item.service;

import com.shopping2.auth.token.JwtService;
import com.shopping2.category.Category;
import com.shopping2.category.CategoryRepository;
import com.shopping2.heart.Heart;
import com.shopping2.heart.HeartRepository;
import com.shopping2.item.Item;
import com.shopping2.item.dto.*;
import com.shopping2.item.repository.ItemRepository;
import com.shopping2.user.UserRepository;
import com.shopping2.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final AwsS3Service awsS3Service;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final HeartRepository heartRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public ItemSaveResponse createItem(ItemRegister item) {

        String url = awsS3Service.uploadImageToS3(item.getMainFile());
        Category category = checkDuplicatedCategory(item.getCategory());
        String itemName = checkDuplicatedItemName(item.getItemName());

        Item storeItem = new Item(itemName, item.getPrice(), category, url, 0);
        itemRepository.save(storeItem);

        return new ItemSaveResponse(storeItem.getName(), storeItem.getPrice(), category.getName(), url);
    }

    private String checkDuplicatedItemName(String itemName) throws DuplicateKeyException {
        if (itemRepository.existsByName(itemName)) {
            throw new DuplicateKeyException("중복된 상품 이름이 존재합니다.");
        } else {
            return itemName;
        }
    }

    private Category checkDuplicatedCategory(String category) {
        if (categoryRepository.existsByName(category)) {
            return categoryRepository.findByName(category);
        } else {
            Category newCategory = new Category(category);
            categoryRepository.save(newCategory);
            return newCategory;
        }
    }

    @Transactional
    public void modifyItem(ItemModify item) {
        Item findItem = itemRepository.findByName(item.getPreviousName())
                .orElseThrow(NoSuchElementException::new);

        Category category = checkDuplicatedCategory(item.getCategory());
        String url = getModifyImageUrl(item.getMainFile(), findItem);
        Category oldCategory = findItem.getCategory();

        findItem.updateItem(item.getItemName(), item.getPrice(), category, url);

        if (!itemRepository.existsByCategory(oldCategory)) {
            categoryRepository.delete(oldCategory);
        }
    }

    private String getModifyImageUrl(MultipartFile mainFile, Item findItem) {
        String url = findItem.getUrl();
        if (mainFile.isEmpty()) {
            return url;
        }
        awsS3Service.deleteImage(url.split("/")[3]);
        return awsS3Service.uploadImageToS3(mainFile);
    }

    @Transactional
    public void deleteItem(ItemDelete item) {
        Item findItem = itemRepository.findByName(item.getItemName())
                .orElseThrow(NoSuchElementException::new);

        awsS3Service.deleteImage(findItem.getUrl().split("/")[3]);
        heartRepository.deleteAllByItem(findItem);
        itemRepository.delete(findItem);

        if (!itemRepository.existsByCategory(findItem.getCategory())) {
            categoryRepository.delete(findItem.getCategory());
        }
    }

    public Page<ItemDto> getHeartItemPage(String jwt, Pageable pageable) {
        String userId = jwtService.subStringBearerAndExtractUserLoginId(jwt);
        User user = userRepository.findByLoginId(userId)
                .orElseThrow(NoSuchElementException::new);

        List<Heart> heartList = heartRepository.findAllByUser(user);

        List<Long> userIdList = new ArrayList<>(); // user 기본키 리스트
        for (Heart heart : heartList) {
            Long id = heart.getItem().getId();
            userIdList.add(id);
        }
        return itemRepository.heartPage(userIdList, pageable);
    }
}


