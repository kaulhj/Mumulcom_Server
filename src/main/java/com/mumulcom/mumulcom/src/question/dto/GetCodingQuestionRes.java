package com.mumulcom.mumulcom.src.question.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetCodingQuestionRes {
    private Long questionIdx;
    private Long userIdx;
    private String nickname;
    private String profileImgUrl;
    private String createdAt;
    private String title;
    private List<String> questionImgUrl;
    private String currentError;
    private String myCodingSkill;
    private String bigCategoryName;
    private String smallCategoryName;
    private int likeCount;
    private int replyCount;
    private String isLiked;
    private String isScraped;
    private String isAdopted;
}
