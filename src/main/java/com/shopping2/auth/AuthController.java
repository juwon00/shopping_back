package com.shopping2.auth;

import com.shopping2.auth.dto.AuthRequest;
import com.shopping2.auth.dto.AuthResponse;
import com.shopping2.auth.dto.RegisterRequest;
import com.shopping2.status.Message;
import com.shopping2.status.StatusEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<Message> register(
            @Valid @RequestBody RegisterRequest request, BindingResult bindingResult
    ) throws DuplicateKeyException {

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            bindingResult.getAllErrors().forEach(objectError -> {
                FieldError field = (FieldError) objectError;
                String message = objectError.getDefaultMessage();

                System.out.println(field.getField() + ": " + message);

                sb.append("field: " + field.getField());
                sb.append("message: " + message);
            });
            return ResponseEntity.badRequest()
                    .headers(new HttpHeaders())
                    .body(new Message(sb));
        }

        AuthResponse result = authService.register(request);

        Message message = new Message(StatusEnum.OK, "성공 코드", result);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        log.info("회원가입 " + result);

        return ResponseEntity.ok()
                .headers(headers)
                .body(message);
    }


    // 로그인
    @PostMapping("/authenticate")
    public ResponseEntity<Message> authenticate(
            @RequestBody AuthRequest request
    ) {
        AuthResponse result = authService.authenticate(request);

        Message message = new Message(StatusEnum.OK, "성공 코드", result);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        log.info("로그인 " + result);

        return ResponseEntity.ok()
                .headers(headers)
                .body(message);
    }


    // refresh-token 가지고 access-token 재발급
    @PostMapping("/refresh-token")
    public ResponseEntity<Message> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        AuthResponse result = authService.refreshToken(request, response);

        Message message = new Message(StatusEnum.OK, "성공 코드", result);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        log.info("access-token 재발급 " + result);

        return ResponseEntity.ok()
                .headers(headers)
                .body(message);
    }

}
