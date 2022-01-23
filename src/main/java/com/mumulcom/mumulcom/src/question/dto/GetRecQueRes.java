package com.mumulcom.mumulcom.src.question.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class GetRecQueRes {
    private long Name;
    private String Created;
    private long Reply;
    private long Like;
    private String BigCategory;
    private String SmallCategory;
    private String Title;
}
