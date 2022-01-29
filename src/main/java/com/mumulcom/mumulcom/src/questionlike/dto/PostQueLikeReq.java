package com.mumulcom.mumulcom.src.questionlike.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class PostQueLikeReq {
    private long questionIdx;
    private long userIdx;
}
