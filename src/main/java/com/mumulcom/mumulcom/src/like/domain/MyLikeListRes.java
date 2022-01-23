package com.mumulcom.mumulcom.src.like.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyLikeListRes {
    private long questionIdx;
    private String writer;
    private String bigCategoryName;
    private String smallCategoryName;
    private String title;
    private String updatedAt;
    private int likeCount;
    private int replyCount;
}
