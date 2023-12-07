package com.shopping2.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ErrorMessage {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private String timestmap;
    private int code;
    private String status;
    private String message;
    private String path;


    public ErrorMessage(int code, String status, String message, String path) {
        LocalDateTime now = LocalDateTime.now();
        this.timestmap = String.valueOf(now);
        this.code = code;
        this.status = status;
        this.message = message;
        this.path = path;
    }

    public String convertToJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }

    public static ResponseEntity<ErrorMessage> toResponseEntity(WebRequest request, ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorMessage.builder()
                        .timestmap(String.valueOf(LocalDateTime.now()))
                        .code(errorCode.getHttpStatus().value())
                        .status(String.valueOf(errorCode.getHttpStatus()).split(" ")[1])
                        .message(errorCode.getMessage())
                        .path(request.getDescription(false))
                        .build()
                );
    }
}
