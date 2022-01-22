package com.mumulcom.mumulcom.src.question.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetQuestionListRes {

    private Long questionIdx;
    private Long useIdx;
    private String nickname;
    private String createdAt;
    private String title;
    private String bigCategoryIdx;
    private String smallCategoryIdx;
    private int likeCount;
    private int replyCount;
}
