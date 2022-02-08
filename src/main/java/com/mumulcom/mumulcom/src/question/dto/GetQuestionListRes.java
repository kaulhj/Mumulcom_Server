package com.mumulcom.mumulcom.src.question.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetQuestionListRes {
    private Long questionIdx;
    private Long userIdx;
    private String nickname;
    private String profileImgUrl;
    private String createdAt;
    private String title;
    private String bigCategoryName;
    private String smallCategoryName;
    private int likeCount;
    private int replyCount;
}
