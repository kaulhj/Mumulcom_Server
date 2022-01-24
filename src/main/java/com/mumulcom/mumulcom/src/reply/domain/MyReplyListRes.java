package com.mumulcom.mumulcom.src.reply.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyReplyListRes {
    private long questionIdx;
    private String nickname;
    private String bigCategoryName;
    private String smallCategoryName;
    private String title;
    private String createdAt;
    private int likeCount;
    private int replyCount;
}
