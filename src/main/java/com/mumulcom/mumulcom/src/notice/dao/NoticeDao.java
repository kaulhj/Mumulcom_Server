package com.mumulcom.mumulcom.src.notice.dao;

import com.mumulcom.mumulcom.src.notice.repository.GetNoticeRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class NoticeDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 휘정
     * 알림 조회 API
     * */
    public List<GetNoticeRes> noticeList (int userIdx) {
        String noticeListQuery = "select questionIdx, noticeContent from Notice where userIdx = ?";
        return jdbcTemplate.query(noticeListQuery,
                (rs,rowNum) -> new GetNoticeRes(
                        rs.getLong("questionIdx"),
                        rs.getString("noticeContent")
                ),userIdx);
    }
}
