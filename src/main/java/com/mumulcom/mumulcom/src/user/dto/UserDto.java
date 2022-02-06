package com.mumulcom.mumulcom.src.user.dto;

import com.github.dozermapper.core.Mapping;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class UserDto {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class SignInReq {
        @Email
        @NotBlank
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
        private String group;
        private List<String> myCategories;
        private String profileImgUrl;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class SignUpReq {

        @Mapping("email")
        @NotBlank
        @Email
        private String email;

        @Mapping("name")
        @NotBlank
        private String name;

        @Mapping("nickname")
        @Pattern(regexp = "^[가-힣a-z0-9]+$")
        @Size(min = 2, max = 8)
        private String nickname;

        @Mapping("group")
        @NotBlank
        private String group;

        private List<String> myCategories;
    }

    @Builder
    @Getter
    public static class SignUpRes {
        private Long userIdx;
        private String email;
        private String name;
        private String nickname;
        private List<String> myCategories;
        private String profileImgUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class PatchReq {

        private Long userIdx;

        @Pattern(regexp = "^[가-힣a-z0-9]+$")
        @Size(min = 2, max = 8)
        private String nickname;

        @NotBlank
        private String group;

        private List<String> myCategories;

        private String profileImgUrl;

    }

    @Builder
    @Getter
    public static class UserRes {
        private String email;
        private String name;
        private String nickname;
        private String group;
        private List<String> myCategories;
        private String profileImgUrl;
    }
}
