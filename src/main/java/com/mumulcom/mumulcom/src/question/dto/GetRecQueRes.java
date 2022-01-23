package com.mumulcom.mumulcom.src.question.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class GetRecQueRes {
    private long BigCategory;
    private long SmallCategory;
    private String Name;
    private String Created;
    private long Reply;
    private String Title;
    private long Like;
}
