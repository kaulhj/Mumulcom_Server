package com.mumulcom.mumulcom.src.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCategoryRes {
    private long bigCategoryIdx;
    private String bigCategoryName;
    private long smallCategoryIdx;
    private String smallCategoryName;
}
