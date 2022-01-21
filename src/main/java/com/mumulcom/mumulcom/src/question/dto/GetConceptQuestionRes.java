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
    private String userName;
    private String createdAt;
    private String title;
    private String content;
    private String bigCategoryIdx;
    private String smallCategoryIdx;
    private int like;
    private int replyCount;
}
