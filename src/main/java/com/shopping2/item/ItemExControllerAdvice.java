package com.shopping2.item;

import com.shopping2.status.ErrorResult;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.shopping.item")
public class ItemExControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> exHandler(Exception e) {
        ErrorResult errorResult = new ErrorResult("ITEM-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    // 중복된 상품 이름이 존재할 때
    @ExceptionHandler
    public ResponseEntity<ErrorResult> duplicateKeyExHandler(DuplicateKeyException e) {
        ErrorResult errorResult = new ErrorResult("ITEM-DUPLICATED-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.CONFLICT);
    }
}
