package com.mumulcom.mumulcom.src.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserJwtData {
    private Long userIdx;
    private String jwt;
}


