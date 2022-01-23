package com.mumulcom.mumulcom.src.question.dao;

import com.mumulcom.mumulcom.src.question.domain.SearchCodingQuestionRes;
import com.mumulcom.mumulcom.src.question.domain.SearchConceptQuestionRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SearchQuestionDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<SearchConceptQuestionRes> searchConceptQuestionRes(String keyword) {
        String searchConceptQuestionQuery = "select * \n" +
                "from (select q.questionIdx, nickname, bigCategoryName, smallCategoryName, title, q.createdAt as updatedAt, content, likeCount, replyCount\n" +
                "from Question q, BigCategory b, SmallCategory s , User u, ConceptQuestion c ,\n" +
                "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
                "from Question q left join (select questionIdx, count(*) likeCount from `Like` group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
                "(select q.questionIdx, ifnull(replyCount,0) as replyCount\n" +
                "from Question q left join (select questionIdx, count(*) replyCount from Reply group by questionIdx) r on q.questionIdx = r.questionIdx) r\n" +
                "where q.bigCategoryIdx = b.bigCategoryIdx and q.smallCategoryIdx = s.smallCategoryIdx and u.userIdx = q.userIdx and c.questionIdx = q.questionIdx \n" +
                "and q.questionIdx = l.questionIdx and q.questionIdx = r.questionIdx) as form\n" +
                "where form.title like ? OR form.content like ?";
        String keywordForm = "%" + keyword + "%";
        Object[] keyWordList = new Object[] {keywordForm, keywordForm};
        return this.jdbcTemplate.query(searchConceptQuestionQuery,
                (rs, rowNum) -> new SearchConceptQuestionRes(
                        rs.getLong("questionIdx"),
                        rs.getString("nickname"),
                        rs.getString("bigCategoryName"),
                        rs.getString("smallCategoryName"),
                        rs.getString("title"),
                        rs.getString("updatedAt"),
                        rs.getString("content"),
                        rs.getInt("likeCount"),
                        rs.getInt("replyCount")
                ),keyWordList);
    }

    public List<SearchCodingQuestionRes> searchCodingQuestionRes(String keyword) {
        String searchCodingQuestionQuery = "select *\n" +
                "from (select q.questionIdx, nickname, bigCategoryName, smallCategoryName, title, q.createdAt as updatedAt, c.currentError, c.myCodingSkill, likeCount, replyCount\n" +
                "from Question q, BigCategory b, SmallCategory s , User u, CodeQuestion c ,\n" +
                "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
                "from Question q left join (select questionIdx, count(*) likeCount from `Like` group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
                "(select q.questionIdx, ifnull(replyCount,0) as replyCount\n" +
                "from Question q left join (select questionIdx, count(*) replyCount from Reply group by questionIdx) r on q.questionIdx = r.questionIdx) r\n" +
                "where q.bigCategoryIdx = b.bigCategoryIdx and q.smallCategoryIdx = s.smallCategoryIdx and u.userIdx = q.userIdx and c.questionIdx = q.questionIdx \n" +
                "and q.questionIdx = l.questionIdx and q.questionIdx = r.questionIdx) as form\n" +
                "where form.title like ? OR form.currentError like ? OR form.myCodingSkill like ?";
        String keywordForm = "%" + keyword + "%";
        Object[] keyWordList = new Object[] {keywordForm, keywordForm, keywordForm};
        return this.jdbcTemplate.query(searchCodingQuestionQuery,
                (rs, rowNum) -> new SearchCodingQuestionRes(
                        rs.getLong("questionIdx"),
                        rs.getString("nickname"),
                        rs.getString("bigCategoryName"),
                        rs.getString("smallCategoryName"),
                        rs.getString("title"),
                        rs.getString("updatedAt"),
                        rs.getString("currentError"),
                        rs.getString("myCodingSkill"),
                        rs.getInt("likeCount"),
                        rs.getInt("replyCount")
                ),keyWordList);
    }


}
