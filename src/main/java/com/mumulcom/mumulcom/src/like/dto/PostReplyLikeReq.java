package com.mumulcom.mumulcom.src.like.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class PostReplyLikeReq {
    private Long replyIdx;
    private Long userIdx;
}
