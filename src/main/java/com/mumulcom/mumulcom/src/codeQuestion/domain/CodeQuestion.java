package com.mumulcom.mumulcom.src.codeQuestion.domain;

import com.mumulcom.mumulcom.src.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "CodeQuestion")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CodeQuestion extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codingIdx;
    private Long questionIdx;
    private String currentError;
    private String myCodingSkill;

    @Column(columnDefinition = "varchar(255) default 'Active'")
    private String status;
}
