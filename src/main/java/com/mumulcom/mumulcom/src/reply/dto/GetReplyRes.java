package com.mumulcom.mumulcom.src.reply.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetReplyRes {

    private Long replyIdx;
    private Long questionIdx;
    private Long userIdx;
    private String nickname;
    private String createdAt;
    private String replyUrl;
    private String content;
    private String replyImgUrl;
    private int likeCount;
    private int reReplyCount;
    private String status;
}
