package com.mumulcom.mumulcom.src.scrap.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyScrapListRes {
    private long questionIdx;
    private String profileImgUrl;
    private String nickname;
    private String bigCategoryName;
    private String smallCategoryName;
    private String title;
    private String createdAt;
    private int likeCount;
    private int replyCount;
}
