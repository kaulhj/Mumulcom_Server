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
    public List<GetNoticeRes> noticeList (long userIdx) {

        String noticeListQuery = "select n.questionIdx, noticeContent, profileImgUrl, (select CASE\n" +
                "\twhen((select timestampdiff(DAY,DATE_FORMAT(createdAt, '%y-%m-%d'),DATE_FORMAT(now(), '%y-%m-%d'))) = 0) then '오늘'\n" +
                "    when((select timestampdiff(DAY,DATE_FORMAT(createdAt, '%y-%m-%d'),DATE_FORMAT(now(), '%y-%m-%d'))) = 1) then '어제'\n" +
                "    when((select timestampdiff(DAY,DATE_FORMAT(createdAt, '%y-%m-%d'),DATE_FORMAT(now(), '%y-%m-%d'))) = 2) then '2일전'\n" +
                "    when((select timestampdiff(DAY,DATE_FORMAT(createdAt, '%y-%m-%d'),DATE_FORMAT(now(), '%y-%m-%d'))) = 3) then '3일전'\n" +
                "    when((select timestampdiff(DAY,DATE_FORMAT(createdAt, '%y-%m-%d'),DATE_FORMAT(now(), '%y-%m-%d'))) = 4) then '4일전'\n" +
                "    when((select timestampdiff(DAY,DATE_FORMAT(createdAt, '%y-%m-%d'),DATE_FORMAT(now(), '%y-%m-%d'))) = 5) then '5일전'\n" +
                "    when((select timestampdiff(DAY,DATE_FORMAT(createdAt, '%y-%m-%d'),DATE_FORMAT(now(), '%y-%m-%d'))) = 6) then '6일전'\n" +
                "    when((select timestampdiff(DAY,DATE_FORMAT(createdAt, '%y-%m-%d'),DATE_FORMAT(now(), '%y-%m-%d'))) >= 7 and (select timestampdiff(DAY,DATE_FORMAT(createdAt, '%y-%m-%d'),DATE_FORMAT(now(), '%y-%m-%d'))) <= 13) then '1주전'\n" +
                "    when((select timestampdiff(DAY,DATE_FORMAT(createdAt, '%y-%m-%d'),DATE_FORMAT(now(), '%y-%m-%d'))) >= 14 and (select timestampdiff(DAY,DATE_FORMAT(createdAt, '%y-%m-%d'),DATE_FORMAT(now(), '%y-%m-%d'))) <= 20) then '2주전'\n" +
                "    when((select timestampdiff(DAY,DATE_FORMAT(createdAt, '%y-%m-%d'),DATE_FORMAT(now(), '%y-%m-%d'))) >= 21 and (select timestampdiff(DAY,DATE_FORMAT(createdAt, '%y-%m-%d'),DATE_FORMAT(now(), '%y-%m-%d'))) <= 27) then '3주전'\n" +
                "    when((select timestampdiff(DAY,DATE_FORMAT(createdAt, '%y-%m-%d'),DATE_FORMAT(now(), '%y-%m-%d'))) >= 28 and (select timestampdiff(DAY,DATE_FORMAT(createdAt, '%y-%m-%d'),DATE_FORMAT(now(), '%y-%m-%d'))) <= 59) then '1달전'\n" +
                "    else '3달 넘은 오래된 게시물' \n" +
                "end) as diffTime,createdAt\n" +
                "from Notice n join (select profileImgUrl, q.questionIdx from Question q join User u on q.userIdx = u.userIdx) writerInfo on n.questionIdx = writerInfo.questionIdx\n" +
                "where n.userIdx = ?\n" +
                "order by updatedAt desc";
        return jdbcTemplate.query(noticeListQuery,
                (rs,rowNum) -> new GetNoticeRes(
                        rs.getLong("questionIdx"),
                        rs.getString("profileImgUrl"),
                        rs.getString("noticeContent"),
                        rs.getString("diffTime")
                ),userIdx);
    }
}
