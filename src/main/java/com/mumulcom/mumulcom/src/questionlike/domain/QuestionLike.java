package com.mumulcom.mumulcom.src.questionlike.domain;

import com.mumulcom.mumulcom.src.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "QuestionLike")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class QuestionLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionLikeIdx;
    private Long questionIdx;
    private Long userIdx;

    @Column(columnDefinition = "varchar(255) default 'Active'")
    private String status;
}
