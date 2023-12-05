package com.shopping2.auth;

import com.shopping2.status.ErrorResult;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 현재 작동 안함. 수정 필요
@RestControllerAdvice("com.shopping.auth")
public class AuthExControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> exHandler(Exception e) {
        ErrorResult errorResult = new ErrorResult("AUTH-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    // 회원가입 중복 아이디 예외
    @ExceptionHandler
    public ResponseEntity<ErrorResult> duplicateKeyExHandler(DuplicateKeyException e) {
        ErrorResult errorResult = new ErrorResult("AUTH-DUPLICATED-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.CONFLICT);
    }

    // 없는 아이디 로그인 예외
    @ExceptionHandler
    public ResponseEntity<ErrorResult> AuthenticationExHandler(AuthenticationException e) {
        ErrorResult errorResult = new ErrorResult("AUTH-NOT-CORRECT-EX", "아이디 또는 비밀번호를 확인해주세요");
        return new ResponseEntity<>(errorResult, HttpStatus.UNAUTHORIZED);
    }
}
