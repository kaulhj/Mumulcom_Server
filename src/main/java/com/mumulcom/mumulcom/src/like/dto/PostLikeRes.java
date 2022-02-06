package com.mumulcom.mumulcom.src.like.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class PostLikeRes {
    private long noticeTargetUserIdx;   //좋아요 시 알림갈 유저 인덱스 ,0일시 취소,재취소로 알림 안감
    private String noticeContent;   //좋아요 시 알림갈 알림내용
}
