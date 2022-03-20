package com.mumulcom.mumulcom.src.report.service;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.src.report.dto.PostReportReq;
import com.mumulcom.mumulcom.src.report.dto.PostReportRes;
import com.mumulcom.mumulcom.src.report.repository.ReportRepository;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.mumulcom.mumulcom.config.BaseResponseStatus.*;

@Service
public class ReportService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ReportRepository reportRepository;
    private JwtService jwtService;

    public ReportService(ReportRepository reportRepository, JwtService jwtService) {
        this.reportRepository = reportRepository;
        this.jwtService = jwtService;
    }

    public PostReportRes createReport(PostReportReq postReportReq) throws BaseException {
        try {
            PostReportRes reportIdx = reportRepository.createReport(postReportReq);
            return reportIdx;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
