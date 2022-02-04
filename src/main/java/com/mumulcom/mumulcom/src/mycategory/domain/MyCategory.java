package com.mumulcom.mumulcom.src.mycategory.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicInsert
public class MyCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myCategoryIdx;

    @NotNull
    private Long userIdx;

    @NotNull
    private Long smallCategoryIdx;
}
