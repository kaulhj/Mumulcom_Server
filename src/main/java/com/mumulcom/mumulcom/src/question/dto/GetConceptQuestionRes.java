package com.mumulcom.mumulcom.src.question.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetConceptQuestionRes {

    private Long questionIdx;
    private Long userIdx;
    private String nickname;
    private String profileImgUrl;
    private String createdAt;
    private String title;
    private List<String> questionImgUrl;
    private String content;
    private String bigCategoryName;
    private String smallCategoryName;
    private int like;
    private int replyCount;
}
