package com.mumulcom.mumulcom.src.question.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class GetCodingQuestionRes {
    private Long questionIdx;
    private Long userIdx;
    private String nickname;
    private String createdAt;
    private String title;
    private String currentError;
    private String myCodingSkill;
    private String bigCategoryName;
    private String smallCategoryName;
    private int likeCount;
    private int replyCount;

}
