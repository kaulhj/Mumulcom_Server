package com.mumulcom.mumulcom.src.question.domain;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodeQuestionReq {
    private List<String> imageUrls;
    private String currentError;
    private String myCodingSkill;
    private long bigCategoryIdx;
    private long smallCategoryIdx;
    private String title;

}
