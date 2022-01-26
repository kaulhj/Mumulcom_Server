package com.mumulcom.mumulcom.src.user.dto;

import com.github.dozermapper.core.Mapping;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserDto {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class SignInReq {
        @Email
        private String email;
    }

    @Getter
    @Builder
    public static class SignInRes {
        private final String jwt;
        private Long userIdx;
        private String email;
        private String name;
        private String nickname;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUpReq {
        @NotBlank
        @Email
        @Mapping("email")
        private String email;

        @NotBlank
        @Mapping("name")
        private String name;

        @NotBlank
        @Mapping("nickname")
        @Size(max = 8)
        private String nickname;

        @Mapping("group")
        private String group;
    }

    @Builder
    @Getter
    public static class SignUpRes {
        private Long userIdx;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatchReq {
        private Long userIdx;
        private String nickname;
    }

    @Builder
    @Getter
    public static class UserRes {
        private String email;
        private String name;
        private String nickname;
    }
}
