package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.dto.AuthDto.*;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public UserResponse signup(SignupRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new AuthException("USERNAME_TAKEN", "이미 사용 중인 아이디입니다.");
        }
        User user = User.builder()
                .username(req.getUsername())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .displayName(req.getDisplayName())
                .build();
        userRepository.save(user);
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
                .filter(u -> passwordEncoder.matches(req.getPassword(), u.getPasswordHash()))
                .orElseThrow(() -> new AuthException(
                        "INVALID_CREDENTIALS", "아이디 또는 비밀번호가 올바르지 않습니다."));
        String token = jwtProvider.createToken(user.getId(), user.getUsername());
        return LoginResponse.builder().token(token).user(toResponse(user)).build();
    }

    @Transactional(readOnly = true)
    public UserResponse me(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException("UNAUTHORIZED", "로그인이 필요합니다."));
        return toResponse(user);
    }

    private UserResponse toResponse(User u) {
        return UserResponse.builder()
                .id(u.getId())
                .username(u.getUsername())
                .displayName(u.getDisplayName() != null ? u.getDisplayName() : u.getUsername())
                .build();
    }
}
