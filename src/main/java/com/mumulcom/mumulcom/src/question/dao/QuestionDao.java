package com.mumulcom.mumulcom.src.question.dao;

import com.mumulcom.mumulcom.src.question.dto.GetCodingQuestionRes;
import com.mumulcom.mumulcom.src.question.dto.GetConceptQuestionRes;
import com.mumulcom.mumulcom.src.question.dto.GetQuestionListRes;
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
                "SELECT q.questionIdx, u.userIdx, u.nickname, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS createdAt, q.title, CQ.currentError, CQ.myCodingSkill, q.bigCategoryIdx, q.smallCategoryIdx, ifnull(l.likeCount, 0) likeCount, ifnull(r.replyCount, 0) replyCount\n" +
                        "FROM User u\n" +
                        "INNER JOIN Question q\n" +
                        "on u.userIdx = q.userIdx\n" +
                        "INNER JOIN CodeQuestion CQ on q.questionIdx = CQ.questionIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS likeCount FROM `Like` WHERE questionIdx = ?) l\n" +
                        "ON q.questionIdx = l.questionIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS replyCount FROM Reply WHERE questionIdx = ?) r\n" +
                        "ON q.questionIdx = r.questionIdx\n" +
                        "WHERE q.questionIdx = ?";
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
                "SELECT q.questionIdx, u.userIdx, u.nickname, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS createdAt, q.title, CQ.content, q.bigCategoryIdx, q.smallCategoryIdx, ifnull(l.likeCount, 0) likeCount, ifnull(r.replyCount, 0) replyCount\n" +
                        "FROM User u\n" +
                        "INNER JOIN Question q\n" +
                        "on u.userIdx = q.userIdx\n" +
                        "INNER JOIN ConceptQuestion CQ on q.questionIdx = CQ.questionIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS likeCount FROM `Like` WHERE questionIdx = ?) l\n" +
                        "ON q.questionIdx = l.questionIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS replyCount FROM Reply WHERE questionIdx = ?) r\n" +
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
     * yeji 7번 API
     * 카테고리별 질문 목록 조회 API (최신순, 핫한순 정렬)
     */
    public List<GetQuestionListRes> getQuestionsByCategory(int sort, int bigCategoryIdx, int smallCategoryIdx) {
        String getQuestionsQuery;
        String orderBy = "";
        int getQuestionsParams;

        if (sort == 1) {
            orderBy = "q.createdAt";
        } else if(sort == 2){
            orderBy = "likeCount";
        }

        if(smallCategoryIdx == 0) {
            getQuestionsQuery = "SELECT q.questionIdx, u.userIdx, u.nickname, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS createdAt, q.title, q.bigCategoryIdx, q.smallCategoryIdx, ifnull(l.likeCount, 0) likeCount, ifnull(r.replyCount, 0) replyCount\n" +
                    "FROM User u\n" +
                    "INNER JOIN Question q\n" +
                    "on u.userIdx = q.userIdx\n" +
                    "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS likeCount FROM `Like` group by questionIdx) l\n" +
                    "ON q.questionIdx = l.questionIdx\n" +
                    "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS replyCount FROM Reply group by questionIdx) r\n" +
                    "ON q.questionIdx = r.questionIdx\n" +
                    "where q.bigCategoryIdx = ?\n" +
                    "order by "+ orderBy +" desc";
            getQuestionsParams = bigCategoryIdx;
        } else {
            getQuestionsQuery = "SELECT q.questionIdx, u.userIdx, u.nickname, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS createdAt, q.title, q.bigCategoryIdx, q.smallCategoryIdx, ifnull(l.likeCount, 0) likeCount, ifnull(r.replyCount, 0) replyCount\n" +
                    "FROM User u\n" +
                    "INNER JOIN Question q\n" +
                    "on u.userIdx = q.userIdx\n" +
                    "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS likeCount FROM `Like` group by questionIdx) l\n" +
                    "ON q.questionIdx = l.questionIdx\n" +
                    "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS replyCount FROM Reply group by questionIdx) r\n" +
                    "ON q.questionIdx = r.questionIdx\n" +
                    "where q.smallCategoryIdx = ?\n" +
                    "order by "+ orderBy +" desc";
            getQuestionsParams = smallCategoryIdx;
        }

        return this.jdbcTemplate.query(getQuestionsQuery,
                (rs, rowNum) -> new GetQuestionListRes(
                        rs.getLong("questionIdx"),
                        rs.getLong("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("createdAt"),
                        rs.getString("title"),
                        rs.getString("bigCategoryIdx"),
                        rs.getString("smallCategoryIdx"),
                        rs.getInt("likeCount"),
                        rs.getInt("replyCount")),
                getQuestionsParams);
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
