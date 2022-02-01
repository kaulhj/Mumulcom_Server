package com.mumulcom.mumulcom.src.question.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetConceptQuestionRes {

    private Long questionIdx;
    private Long userIdx;
    private String nickname;
    private String createdAt;
    private String title;
    private String content;
    private String bigCategoryName;
    private String smallCategoryName;
    private int like;
    private int replyCount;
}
