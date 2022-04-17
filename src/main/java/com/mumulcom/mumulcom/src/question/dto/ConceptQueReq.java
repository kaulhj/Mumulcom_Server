package com.mumulcom.mumulcom.src.question.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class ConceptQueReq {
    //private List<String> images;
    private long userIdx;
    private String content;
    private long bigCategoryIdx;
    private long smallCategoryIdx;
    private String title;

}
