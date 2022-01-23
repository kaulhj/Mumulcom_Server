package com.mumulcom.mumulcom.src.like.dao;

import com.mumulcom.mumulcom.src.like.domain.MyLikeListRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MyLikeListDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

//    public List<MyLikeListRes> myLikeListResList(int userIdx) {
//        String myLikeListQuery = "select questionList.questionIdx, questionList.writer, bigCategoryName, smallCategoryName, title, questionList.createdAt, likeCount, replyCount\n" +
//                "from \n" +
//                "(select q.questionIdx, nickname as writer, bigCategoryName, smallCategoryName, title, q.createdAt, likeCount, replyCount\n" +
//                "from Question q, BigCategory b, SmallCategory s , User u, CodeQuestion c ,\n" +
//                "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
//                "from Question q left join (select questionIdx, count(*) likeCount from `Like` group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
//                "(select q.questionIdx, ifnull(replyCount,0) as replyCount\n" +
//                "from Question q left join (select questionIdx, count(*) replyCount from Reply group by questionIdx) r on q.questionIdx = r.questionIdx) r\n" +
//                "where q.bigCategoryIdx = b.bigCategoryIdx and q.smallCategoryIdx = s.smallCategoryIdx and u.userIdx = q.userIdx and c.questionIdx = q.questionIdx \n" +
//                "and q.questionIdx = l.questionIdx and q.questionIdx = r.questionIdx\n" +
//                "union\n" +
//                "select q.questionIdx,nickname as writer, bigCategoryName, smallCategoryName, title, q.createdAt, likeCount, replyCount\n" +
//                "from Question q, BigCategory b, SmallCategory s , User u, ConceptQuestion c ,\n" +
//                "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
//                "from Question q left join (select questionIdx, count(*) likeCount from `Like` group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
//                "(select q.questionIdx, ifnull(replyCount,0) as replyCount\n" +
//                "from Question q left join (select questionIdx, count(*) replyCount from Reply group by questionIdx) r on q.questionIdx = r.questionIdx) r\n" +
//                "where q.bigCategoryIdx = b.bigCategoryIdx and q.smallCategoryIdx = s.smallCategoryIdx and u.userIdx = q.userIdx and c.questionIdx = q.questionIdx \n" +
//                "and q.questionIdx = l.questionIdx and q.questionIdx = r.questionIdx) questionList join `Like` l on questionList.questionIdx = l.questionIdx\n" +
//                "where userIdx = 5 and bigCategoryName = ? and smallCategoryName = ?"
//    }
}
