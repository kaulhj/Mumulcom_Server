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
    }

    @Getter
    @Builder
    public static class SignUpReq {
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email
        @Mapping("email")
        private String email;

        @NotBlank(message = "이름을 입력해주세요.")
        @Mapping("name")
        private String name;

        @Mapping("birth")
        private String birth;

        @NotBlank(message = "닉네임을 입력해주세요.")
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
}
