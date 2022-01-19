package com.mumulcom.mumulcom.src.user.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
public class UserSignUpData {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email
    @Mapping("userEmail")
    private String email;

    @NotBlank(message = "이름을 입력해주세요.")
    @Mapping("userName")
    private String name;

    @Mapping("userBirth")
    private String birth;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Mapping("nickname")
    @Size(max=8)
    private String nickname;

//    @Mapping("group")
//    private String group;
}
