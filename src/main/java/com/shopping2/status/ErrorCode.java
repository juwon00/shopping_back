package com.shopping2.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    CUSTOM_ERROR(HttpStatus.BAD_REQUEST, "알 수 없는 이상한 에러"),

    // auth
    NOT_VALIDATED_USER(HttpStatus.BAD_REQUEST, "형식에 맞게 입력해 주세요."),
    DUPLICATED_USER_LOGIN_ID(HttpStatus.CONFLICT, "이미 사용중인 아이디 입니다."),
    NOT_EXIST_USER_LOGIN_ID(HttpStatus.CONFLICT, "아이디 혹은 비밀번호가 틀립니다."),

    // heart
    NOT_EXIST_HEART_USER(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    NOT_EXIST_HEART_ITEM(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),
    ALREADY_HEART(HttpStatus.CONFLICT, "이미 좋아요를 누른 상태 입니다."),
    ALREADY_UNHEART(HttpStatus.CONFLICT, "좋아요를 안 누른 상태 입니다."),

    // item
    DUPLICATED_ITEM_NAME(HttpStatus.CONFLICT, "중복된 상품 이름이 존재합니다."),
    NO_EXIST_USER(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    NO_EXIST_ITEM(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
