package com.mumulcom.mumulcom.src.replyImage.domain;

import com.mumulcom.mumulcom.src.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ReplyImage")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ReplyImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reImgIdx;
    private Long replyIdx;
    private String url;

    @Column(columnDefinition = "varchar(255) default 'Active'")
    private String status;
}
