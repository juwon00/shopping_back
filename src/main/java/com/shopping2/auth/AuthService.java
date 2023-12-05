package com.shopping2.auth;

import com.shopping2.auth.dto.AuthRequest;
import com.shopping2.auth.dto.AuthResponse;
import com.shopping2.auth.dto.RegisterRequest;
import com.shopping2.auth.token.JwtService;
import com.shopping2.auth.token.TokenRepository;
import com.shopping2.auth.token.model.Token;
import com.shopping2.auth.token.model.TokenType;
import com.shopping2.user.UserRepository;
import com.shopping2.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.shopping2.user.model.Role.USER;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthResponse register(RegisterRequest request) throws DuplicateKeyException {

        if (userRepository.existsByLoginId(request.getLoginId())) {
            // 중복 아이디가 이미 존재하는 경우 예외 처리 또는 에러 메시지 반환
            throw new DuplicateKeyException("이미 사용 중인 아이디입니다.");
        }

        var user = User.builder()
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .email(request.getEmail())
                .role(USER)
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user, user.getRole());
        var refreshToken = jwtService.generateRefreshToken(user, user.getRole());
        saveUserToken(savedUser, jwtToken);
        return AuthResponse.builder()
                .userId(user.getLoginId())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLoginId(),
                        request.getPassword()
                )
        );
        // 여기까지오면 아이디와 비밀번호과 정확하다는 의미

        var user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user, user.getRole());
        var refreshToken = jwtService.generateRefreshToken(user, user.getRole());
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthResponse.builder()
                .userId(user.getLoginId())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


    public AuthResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userLoginId;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        refreshToken = authHeader.substring(7);
        userLoginId = jwtService.extractUserName(refreshToken);
        if (userLoginId != null) {
            var user = this.userRepository.findByLoginId(userLoginId)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user, user.getRole());
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);

                return AuthResponse.builder()
                        .userId(user.getLoginId())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
        }
        return null;
    }
}
