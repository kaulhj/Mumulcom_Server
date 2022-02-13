package com.mumulcom.mumulcom.src.reply.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReplyReq {
    private Long questionIdx;
    private Long userIdx;
    private String replyUrl;
    private String content;
    private List<String> images;
}
