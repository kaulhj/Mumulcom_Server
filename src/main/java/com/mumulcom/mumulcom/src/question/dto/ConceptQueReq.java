package com.mumulcom.mumulcom.src.question.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
public class ConceptQueReq {
    private long userIdx;
    private List<String> imageUrls;
    private String currentError;
    private String myCodingSkill;
    private long bigCategoryIdx;
    private long smallCategoryIdx;
    private String title;

}
