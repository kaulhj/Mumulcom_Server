package com.mumulcom.mumulcom.src.reply.domain;

import com.mumulcom.mumulcom.src.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Reply")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Reply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyIdx;
    private Long questionIdx;
    private Long userIdx;
    private String content;

    @Column(columnDefinition = "varchar(255) default 'Active'")
    private String status;
}
