package com.mumulcom.mumulcom.src.mycategory.dto;

import com.github.dozermapper.core.Mapping;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MyCategoryDto {

    @Mapping("userIdx")
    @NotNull
    private Long userIdx;

    @Mapping("smallCategoryIdx")
    @NotNull
    private Long smallCategoryIdx;
}
