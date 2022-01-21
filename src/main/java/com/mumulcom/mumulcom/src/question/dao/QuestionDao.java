package com.mumulcom.mumulcom.src.question.dao;

import com.mumulcom.mumulcom.src.question.dto.GetCodingQuestionRes;
import com.mumulcom.mumulcom.src.question.dto.GetConceptQuestionRes;
import com.mumulcom.mumulcom.src.question.dto.GetQuestionRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class QuestionDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public QuestionDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 5번 API
     * 코딩 질문 조회
     */
    public List<GetCodingQuestionRes> getCodingQuestions(int questionIdx) {
        String getCodingQuestionQuery =
                "SELECT q.questionIdx, u.userIdx, u.nickname, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS createdAt, q.title, CQ.currentError, CQ.myCodingSkill, q.bigCategoryIdx, q.smallCategoryIdx, l.likeCount, r.replyCount" +
                " FROM User u" +
                " INNER JOIN Question q" +
                " on u.userIdx = q.userIdx" +
                " INNER JOIN CodeQuestion CQ on q.questionIdx = CQ.questionIdx" +
                " INNER JOIN (SELECT questionIdx, count(questionIdx) likeCount FROM `Like` WHERE questionIdx =?) l" +
                " ON q.questionIdx = l.questionIdx" +
                " INNER JOIN (SELECT questionIdx, count(questionIdx) replyCount FROM Reply WHERE questionIdx =?) r" +
                " ON q.questionIdx = r.questionIdx" +
                " WHERE q.questionIdx =?";
        int getCodingQuestionParams = questionIdx;
        return this.jdbcTemplate.query(getCodingQuestionQuery,
                (rs, rowNum) -> new GetCodingQuestionRes(
                        rs.getLong("questionIdx"),
                        rs.getLong("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("createdAt"),
                        rs.getString("title"),
                        rs.getString("currentError"),
                        rs.getString("myCodingSkill"),
                        rs.getString("bigCategoryIdx"),
                        rs.getString("smallCategoryIdx"),
                        rs.getInt("likeCount"),
                        rs.getInt("replyCount")),
                getCodingQuestionParams, getCodingQuestionParams, getCodingQuestionParams);
    }

    /**
     * 6번 API
     * 개념 질문 조회
     */
    public List<GetConceptQuestionRes> getConceptQuestions(int questionIdx) {
        String getConceptQuestionQuery =
                "SELECT q.questionIdx, u.userIdx, u.nickname, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS createdAt, q.title, CQ.content, q.bigCategoryIdx, q.smallCategoryIdx, l.likeCount, r.replyCount\n" +
                        "FROM User u\n" +
                        "INNER JOIN Question q\n" +
                        "on u.userIdx = q.userIdx\n" +
                        "INNER JOIN ConceptQuestion CQ on q.questionIdx = CQ.questionIdx\n" +
                        "INNER JOIN (SELECT questionIdx, count(questionIdx) AS likeCount FROM `Like` WHERE questionIdx = ?) l\n" +
                        "ON q.questionIdx = l.questionIdx\n" +
                        "INNER JOIN (SELECT questionIdx, count(questionIdx) AS replyCount FROM Reply WHERE questionIdx = ?) r\n" +
                        "ON q.questionIdx = r.questionIdx\n" +
                        "WHERE q.questionIdx = ?";
        int getConceptQuestionParams = questionIdx;
        return this.jdbcTemplate.query(getConceptQuestionQuery,
                (rs, rowNum) -> new GetConceptQuestionRes(
                        rs.getLong("questionIdx"),
                        rs.getLong("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("createdAt"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("bigCategoryIdx"),
                        rs.getString("smallCategoryIdx"),
                        rs.getInt("likeCount"),
                        rs.getInt("replyCount")),
                getConceptQuestionParams, getConceptQuestionParams, getConceptQuestionParams);
    }


    /**
     * yeji test API
     * questionIdx를 이용한 특정 질문 조회
     */

    public List<GetQuestionRes> getQuestions(int questionIdx) {
        String getQuestionQuery = "SELECT * FROM Question WHERE questionIdx =?";
        int getQuestionParams = questionIdx;
        return this.jdbcTemplate.query(getQuestionQuery,
                (rs, rowNum) -> new GetQuestionRes(
                        rs.getLong("questionIdx"),
                        rs.getLong("userIdx")),
                getQuestionParams);
    }
}
