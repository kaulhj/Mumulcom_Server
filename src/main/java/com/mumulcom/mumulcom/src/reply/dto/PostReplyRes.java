package com.mumulcom.mumulcom.src.reply.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReplyRes {
    private Long replyIdx;
    private String replyImgResult;
    private String noticeReply;
    private Long questionUserIdx;
    private String noticeScrap;
    private List<Long> scrapUserIdx;
}
