package com.mumulcom.mumulcom.src.report.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReportReq {
    private Long reporterUserIdx;
    private Long reportedUserIdx;
    private Long reportTypeIdx;
    private String reportContent;
}
