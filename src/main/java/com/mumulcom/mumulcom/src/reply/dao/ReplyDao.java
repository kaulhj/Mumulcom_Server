package com.mumulcom.mumulcom.src.reply.dao;

import com.mumulcom.mumulcom.src.reply.domain.MyReplyListRes;
import com.mumulcom.mumulcom.src.reply.dto.PostReplyReq;
import com.mumulcom.mumulcom.src.reply.dto.PostReplyRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReplyDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ReplyDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * yeji 8번 API
     * 답변 생성
     */
    public PostReplyRes creatReply(PostReplyReq postReplyReq) {

        String replyImgResult;

        // Reply table insert
        String createReplyQuery = "insert into Reply(questionIdx, userIdx, content) values (?, ?, ?)";
        Object[] createReplyParams = new Object[]{postReplyReq.getQuestionIdx(), postReplyReq.getUserIdx(), postReplyReq.getContent()};
        this.jdbcTemplate.update(createReplyQuery, createReplyParams);

        // 마지막으로 삽입된 ReplyIdx 값 추출
        String lastInsertIdQuery = "select last_insert_id()";
        Long replyIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery, Long.class);
        replyImgResult = "해당 답변은 첨부된 이미지가 없습니다.";

        if(postReplyReq.getUrl() != null) {

            // ReplyImg table insert
            String createReplyImgQuery = "insert into ReplyImage(replyIdx, url) value (?, ?)";
            Long replyImgIdx;
            for(String url : postReplyReq.getUrl()) {
                Object[] createReplyImgParams = new Object[]{replyIdx, url};
                this.jdbcTemplate.update(createReplyImgQuery, createReplyImgParams);
            }
            replyImgResult = "이미지 삽입이 완료됐습니다.";
        }

        return new PostReplyRes(replyIdx, replyImgResult);
    }

    /**
     * 휘정
     * 내가 답변한 질문 리스트 조회 API
     * */
    public List<MyReplyListRes> myReplyListResList(int userIdx) {
        String myReplyListQuery = "select  questionList.questionIdx, nickname, bigCategoryName, smallCategoryName, title, questionList.createdAt, likeCount, replyCount\n" +
                "from\n" +
                "(select q.questionIdx, nickname, bigCategoryName, smallCategoryName, title, q.createdAt, likeCount, replyCount\n" +
                "from Question q, BigCategory b, SmallCategory s , User u, CodeQuestion c ,\n" +
                "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
                "from Question q left join (select questionIdx, count(*) likeCount from QuestionLike group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
                "(select q.questionIdx, ifnull(replyCount,0) as replyCount\n" +
                "from Question q left join (select questionIdx, count(*) replyCount from Reply group by questionIdx) r on q.questionIdx = r.questionIdx) r\n" +
                "where q.bigCategoryIdx = b.bigCategoryIdx and q.smallCategoryIdx = s.smallCategoryIdx and u.userIdx = q.userIdx and c.questionIdx = q.questionIdx \n" +
                "and q.questionIdx = l.questionIdx and q.questionIdx = r.questionIdx\n" +
                "union\n" +
                "select q.questionIdx,nickname, bigCategoryName, smallCategoryName, title, q.createdAt, likeCount, replyCount\n" +
                "from Question q, BigCategory b, SmallCategory s , User u, ConceptQuestion c ,\n" +
                "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
                "from Question q left join (select questionIdx, count(*) likeCount from QuestionLike group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
                "(select q.questionIdx, ifnull(replyCount,0) as replyCount\n" +
                "from Question q left join (select questionIdx, count(*) replyCount from Reply group by questionIdx) r on q.questionIdx = r.questionIdx) r\n" +
                "where q.bigCategoryIdx = b.bigCategoryIdx and q.smallCategoryIdx = s.smallCategoryIdx and u.userIdx = q.userIdx and c.questionIdx = q.questionIdx \n" +
                "and q.questionIdx = l.questionIdx and q.questionIdx = r.questionIdx) questionList join Reply r on questionList.questionIdx = r.questionIdx\n" +
                "where r.userIdx = ?";
        return this.jdbcTemplate.query(myReplyListQuery,
                (rs,rowNum) -> new MyReplyListRes(
                        rs.getLong("questionIdx"),
                        rs.getString("nickname"),
                        rs.getString("bigCategoryName"),
                        rs.getString("smallCategoryName"),
                        rs.getString("title"),
                        rs.getString("createdAt"),
                        rs.getInt("likeCount"),
                        rs.getInt("replyCount")
                ), userIdx);
    }

}
