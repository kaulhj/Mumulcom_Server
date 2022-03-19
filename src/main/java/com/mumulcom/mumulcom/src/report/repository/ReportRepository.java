package com.mumulcom.mumulcom.src.report.repository;

import com.mumulcom.mumulcom.src.report.dto.PostReportReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ReportRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ReportRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 예지
     * 신고하기 API
     */
    public Long createReport(PostReportReq postReportReq) {
        String createReportQuery = "INSERT INTO Report(reporterUserIdx, reportedUserIdx, reportTypeIdx, reportContent)\n" +
                "VALUES (?, ?, ?, ?);";
        Object[] createReportParams = new Object[]{postReportReq.getReporterUserIdx(), postReportReq.getReportedUserIdx(), postReportReq.getReportTypeIdx(), postReportReq.getReportContent()};

        this.jdbcTemplate.update(createReportQuery, createReportParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, Long.class);
    }
}
