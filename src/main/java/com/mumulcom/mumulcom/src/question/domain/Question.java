package com.mumulcom.mumulcom.src.question.domain;

import com.mumulcom.mumulcom.src.BaseTimeEntity;
import com.mumulcom.mumulcom.src.question.dto.GetCodingQuestionRes;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Question")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Question extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionIdx;
    private Long userIdx;
    private String bigCategoryIdx;
    private String smallCategoryIdx;
    private String title;

    @Column(columnDefinition = "varchar(255) default 'Active'")
    private String status;
}
