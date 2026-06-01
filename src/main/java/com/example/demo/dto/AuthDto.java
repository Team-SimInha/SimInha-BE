package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

public class AuthDto {

    @Getter @NoArgsConstructor @AllArgsConstructor
    public static class SignupRequest {
        @NotBlank private String username;
        @NotBlank @Size(min = 4) private String password;
        private String displayName;
    }

    @Getter @NoArgsConstructor @AllArgsConstructor
    public static class LoginRequest {
        @NotBlank private String username;
        @NotBlank private String password;
    }

    @Getter @AllArgsConstructor @Builder
    public static class UserResponse {
        private Long id;
        private String username;
        private String displayName;
    }

    @Getter @AllArgsConstructor @Builder
    public static class LoginResponse {
        private String token;
        private UserResponse user;
    }
}
