package com.shopping2.auth;

import com.shopping2.auth.dto.AuthRequest;
import com.shopping2.auth.dto.AuthResponse;
import com.shopping2.auth.dto.RegisterRequest;
import com.shopping2.error.CustomException;
import com.shopping2.status.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.shopping2.status.ErrorCode.NOT_VALIDATED_USER;

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
    ) throws DuplicateKeyException, CustomException {

        if (bindingResult.hasErrors()) {
            throw new CustomException(NOT_VALIDATED_USER);
        }
        AuthResponse result = authService.register(request);

        log.info("회원가입 " + result);
        return Message.MessagetoResponseEntity(result);
    }


    // 로그인
    @PostMapping("/authenticate")
    public ResponseEntity<Message> authenticate(
            @RequestBody AuthRequest request
    ) {
        AuthResponse result = authService.authenticate(request);

        log.info("로그인 " + result);
        return Message.MessagetoResponseEntity(result);
    }


    // refresh-token 가지고 access-token 재발급
    @PostMapping("/refresh-token")
    public ResponseEntity<Message> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        AuthResponse result = authService.refreshToken(request, response);

        log.info("access-token 재발급 " + result);
        return Message.MessagetoResponseEntity(result);
    }

}
