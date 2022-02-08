package com.mumulcom.mumulcom.src.reply.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetReplyRes {

    private Long replyIdx;
    private Long questionIdx;
    private Long userIdx;
    private String nickname;
    private String profileImgUrl;
    private String createdAt;
    private String replyUrl;
    private String content;
    private List<String> replyImgUrl;
    private int likeCount;
    private int reReplyCount;
    private String status;
}
