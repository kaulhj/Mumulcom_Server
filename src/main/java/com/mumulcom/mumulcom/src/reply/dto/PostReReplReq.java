package com.mumulcom.mumulcom.src.reply.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReReplReq {
    private Long replyIdx;
    private Long userIdx;
    private String content;
    private String imageUrl;

}
