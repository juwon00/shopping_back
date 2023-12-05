package com.shopping2.heart;

import com.shopping2.status.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice("com.shopping.heart")
public class HeartExControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> exhandler(Exception e) {
        ErrorResult errorResult = new ErrorResult("HEART-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    // 좋아요 누른 상태에서 다시 좋아요 or 좋아요 안 누른 상태에서 안 좋아요
    @ExceptionHandler
    public ResponseEntity<ErrorResult> IllegalStateExhandler(IllegalStateException e) {
        ErrorResult errorResult = new ErrorResult("HEART-ILLEGAL-STATE-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.CONFLICT);
    }

    // 회원이나 아이템을 못찾았을 때 예외
    @ExceptionHandler
    public ResponseEntity<ErrorResult> NoSuchElementExhandler(NoSuchElementException e) {
        ErrorResult errorResult = new ErrorResult("HEART-NO-SUCH_ELEMENT-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.CONFLICT);
    }
}
