package com.mumulcom.mumulcom.src.like.domain;

import com.mumulcom.mumulcom.src.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Like")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Like extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeIdx;
    private Long questionIdx;
    private Long userIdx;

    @Column(columnDefinition = "varchar(255) default 'Active'")
    private String status;
}
