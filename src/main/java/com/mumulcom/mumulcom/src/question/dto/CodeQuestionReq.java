package com.mumulcom.mumulcom.src.question.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data   //7번
public class CodeQuestionReq {
    private List<String> images;
    private long userIdx;
    private String currentError;
    private String myCodingSkill;
    private long bigCategoryIdx;
    private long smallCategoryIdx;
    private String title;
    private String codeQuestionUrl;

}
