package com.mumulcom.mumulcom.src.question.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetRecQueRes {
    private int Number;    //최근질문 순서, 가장 최근
    private Long questionIdx;   //
    private int type;       //개념, 코딩질문 구분, 코딩이면 1
    private String BigCategoryName;
    private String SmallCategoryName;
    private String nickname;
    private String CreatedAt;
    private String Title;
    private int ReplyCount;
    private int LikeCount;
    private String profileImgUrl;   //프로필이미지사진
}
