package com.mumulcom.mumulcom.src.reply.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostReplyRes {
    private Long replyIdx;
    private String replyImgResult;
    private String noticeReply;
    private Long questionUserIdx;
    private String noticeScrap;
    private List<Long> scrapUserIdx;
}
