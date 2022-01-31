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
    private String bigCategoryName;
    private String smallCategoryName;
    private int likeCount;
    private int replyCount;
}
