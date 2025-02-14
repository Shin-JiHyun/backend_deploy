package com.example.backend.user.model;

import lombok.Builder;
import lombok.Getter;

public class UserDto {
    @Getter
    @Builder
    public static class SignupRequest {

        private String email;
        private String password;
        private String nickName;

        public User toEntity(String encodedPassword) {
            return User.builder()
                    .email(email)
                    .password(encodedPassword)
                    .build();
        }
    }
}
