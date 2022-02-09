package com.mumulcom.mumulcom.src.like.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class PostQueLikeReq {
    private Long questionIdx;
    private Long userIdx;
}
