package com.mumulcom.mumulcom.src.question.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class GetCodingQuestionRes {
    private Long questionIdx;
    private Long userIdx;
    private String name;
    private String createdAt;
    private String title;
    private String currentError;
    private String myCodingSkill;
    private String bigCategoryIdx;
    private String smallCategoryIdx;
    private int likeCount;
    private int replyCount;

}
