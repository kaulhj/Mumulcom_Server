package com.mumulcom.mumulcom.src.QuestionLike.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class PostLikeReq {
    private long questionIdx;
    private long userIdx;
    private long replyIdx;
}
