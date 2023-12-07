package com.shopping2.error;

import com.shopping2.status.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorMessage> handleCustomException(WebRequest request, CustomException e) {
        return ErrorMessage.toResponseEntity(request, e.getErrorCode());
    }
}
