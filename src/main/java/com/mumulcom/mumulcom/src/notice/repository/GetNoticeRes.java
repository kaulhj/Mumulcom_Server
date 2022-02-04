package com.mumulcom.mumulcom.src.notice.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetNoticeRes {
    private long questionIdx;
    private String profileImgUrl;
    private String noticeContent;
    private String diffTime;
}
