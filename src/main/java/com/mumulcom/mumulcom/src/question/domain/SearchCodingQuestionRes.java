package com.mumulcom.mumulcom.src.question.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchCodingQuestionRes {
    private long questionIdx;
    private String profileImgUrl;
    private String nickname;
    private String bigCategoryName;
    private String smallCategoryName;
    private String title;
    private String updatedAt;
    private String currentError;
    private String myCodingSkill;
    private int likeCount;
    private int replyCount;
}
