package com.mumulcom.mumulcom.src.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;

@Getter
@Builder
@AllArgsConstructor
public class UserSignInData {

    @Email
    private String email;
}
