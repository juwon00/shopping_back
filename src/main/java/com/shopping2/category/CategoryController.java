package com.shopping2.category;

import com.shopping2.status.Message;
import com.shopping2.status.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping()
    public ResponseEntity<Message> getCategory() {
        List<Category> categoryList = categoryRepository.findAll();

        List<String> categoryNameList = new ArrayList<>();
        for (Category category : categoryList) {
            String name = category.getName();
            categoryNameList.add(name);
        }

        Message message = new Message(StatusEnum.OK, "성공 코드", categoryNameList);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));


        return ResponseEntity.ok()
                .headers(headers)
                .body(message);
    }
}
