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
        String noticeListQuery = "select questionIdx, noticeContent, (select CASE\n" +
                "\twhen((select updatedAt between date_add(now(),interval -1 day) and NOW())) then '오늘'\n" +
                "    when((select updatedAt between date_add(now(),interval -2 day) and date_add(now(),interval -1 day))) then '어제'\n" +
                "    when((select updatedAt between date_add(now(),interval -3 day) and date_add(now(),interval -2 day))) then '2일전'\n" +
                "    when((select updatedAt between date_add(now(),interval -4 day) and date_add(now(),interval -3 day))) then '3일전'\n" +
                "    when((select updatedAt between date_add(now(),interval -5 day) and date_add(now(),interval -4 day))) then '4일전'\n" +
                "    when((select updatedAt between date_add(now(),interval -6 day) and date_add(now(),interval -5 day))) then '5일전'\n" +
                "    when((select updatedAt between date_add(now(),interval -7 day) and date_add(now(),interval -6 day))) then '6일전'\n" +
                "    when((select updatedAt between date_add(now(),interval -14 day) and date_add(now(),interval -7 day))) then '1주전'\n" +
                "    when((select updatedAt between date_add(now(),interval -21 day) and date_add(now(),interval -14 day))) then '2주전'\n" +
                "    when((select updatedAt between date_add(now(),interval -31 day) and date_add(now(),interval -21 day))) then '3주전'\n" +
                "    when((select updatedAt between date_add(now(),interval -2 month ) and date_add(now(),interval -1 month))) then '1달전'\n" +
                "    when((select updatedAt between date_add(now(),interval -3 month ) and date_add(now(),interval -2 month))) then '2달전'\n" +
                "    when((select updatedAt between date_add(now(),interval -4 month ) and date_add(now(),interval -3 month))) then '3달전'\n" +
                "    else '3달 넘은 오래된 게시물' \n" +
                "end) as diffTime from Notice where userIdx = ?";
        return jdbcTemplate.query(noticeListQuery,
                (rs,rowNum) -> new GetNoticeRes(
                        rs.getLong("questionIdx"),
                        rs.getString("noticeContent"),
                        rs.getString("diffTime")
                ),userIdx);
    }
}
