package com.mumulcom.mumulcom.src.reply.dao;

import com.mumulcom.mumulcom.src.reply.domain.MyReplyListRes;
import com.mumulcom.mumulcom.src.reply.domain.ReplyInfoRes;
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

    // 해당 답변 idx를 가지고 질문자 알아내기
    public ReplyInfoRes getReplyInfo(int replyIdx) {
        String getQuestionWriterQuery = "select q.userIdx as writer, q.questionIdx\n" +
                "from Reply r join Question q on r.questionIdx = q.questionIdx\n" +
                "where replyIdx = ?";
        return this.jdbcTemplate.queryForObject(getQuestionWriterQuery,
                (rs,rowNum) -> new ReplyInfoRes(
                        rs.getLong("writer"),
                        rs.getLong("questionIdx")
                )
                ,replyIdx);
    }

    /**
     * 휘정 채택하기 API
     * 채택된 답변의 status는 adopted
     * */
    public int adoptReply (int replyIdx) {
        String adoptReplyQuery = "update Reply set status = 'adopted' where replyIdx = ?";
        return this.jdbcTemplate.update(adoptReplyQuery,replyIdx); // 대응시켜 매핑시켜 쿼리 요청(생성했으면 1, 실패했으면 0)
    }

    /**
     * 휘정
     * 채택된 답변에 대한 알림 생성
     * "회원님이 한 답변이 채택되었습니다."
     * */
    public int addAdoptionNotice (ReplyInfoRes replyInfoRes, String content) {
        String adoptionNoticeQuery = "insert into Notice (NoticeCategoryIdx, questionIdx, userIdx, noticeContent) values (7,?,?,?)";
        Object[] adoptionNoticeParams = new Object[] {replyInfoRes.getQuestionIdx(), replyInfoRes.getWriter(), content};
        this.jdbcTemplate.update(adoptionNoticeQuery, adoptionNoticeParams);
        String lastInsertNoticeIdx = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertNoticeIdx, int.class); // 해당 쿼리문의 결과 마지막으로 삽인된 유저의 userIdx번호를 반환한다.

    }
}
