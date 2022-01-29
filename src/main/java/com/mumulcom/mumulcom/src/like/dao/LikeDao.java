package com.mumulcom.mumulcom.src.like.dao;


import com.mumulcom.mumulcom.src.like.dto.PostQueLikeReq;
import com.mumulcom.mumulcom.src.like.dto.PostReplyLikeReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository

public class LikeDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String createLike(PostQueLikeReq postQueLikeReq){

            String createLikeQuery = "INSERT INTO QuestionLike(QUESTIONIDX, USERIDX) VALUES (?, ?)";
            Object[] createLikeParams = new Object[]{postQueLikeReq.getQuestionIdx(),
            postQueLikeReq.getUserIdx()};
            this.jdbcTemplate.update(createLikeQuery,createLikeParams);

            String creLikNotQuery = "INSERT INTO Notice (noticeCategoryIdx,questionIdx,userIdx,noticeContent)\n" +
                    "VALUES (?,?,?,?)";

            int noticeCategory; //알림 카테고리 구하기
             String lastContent = null;

        lastContent = new String(" 질문에 좋아요를 눌렀습니다.");
            String nContentQuery = "SELECT U.name,Q.title\n" +
                    "FROM `QuestionLike` L\n" +
                    "INNER JOIN Question Q on L.questionIdx = Q.questionIdx\n" +
                    "INNER JOIN User U on L.userIdx = U.userIdx" +
                    " where L.questionIdx = ? AND L.userIdx = ?\n";
            Object[] nContentParams = new Object[]{postQueLikeReq.getQuestionIdx(), postQueLikeReq.getUserIdx()};
        //유저이름, 타이틀 저장
        List<Map<String,Object>> nContentParam2 = this.jdbcTemplate.queryForList(nContentQuery,
                nContentParams);


        String nContent = new String(nContentParam2.get(0).get("name")+" 님이 회원님의 "+
                "질문을 좋아합니다");



        Object[] creLikeNotParams = new Object[]{2, postQueLikeReq.getQuestionIdx(), postQueLikeReq.getUserIdx(),
            nContent};

        this.jdbcTemplate.update(creLikNotQuery,creLikeNotParams);
            return nContent;

    }


    //답변 좋아요
    public String createReplyLike(PostReplyLikeReq postReplyLikeReq){

        String createLikeQuery = "INSERT INTO ReplyLike(ReplyIdx, USERIDX) VALUES (?, ?)";
        Object[] createLikeParams = new Object[]{postReplyLikeReq.getReplyIdx(),
                postReplyLikeReq.getUserIdx()};
        this.jdbcTemplate.update(createLikeQuery,createLikeParams);

        String creLikNotQuery = "INSERT INTO Notice (noticeCategoryIdx,questionIdx,userIdx,noticeContent)\n" +
                "VALUES (?,?,?,?)";

        int noticeCategory; //알림 카테고리 구하기
        String lastContent = null;

        lastContent = new String(" 답변에 좋아요를 눌렀습니다.");


        //
        String nContentQuery = "SELECT U.name,Q.title\n" +
                "FROM `QuestionLike` L\n" +
                "INNER JOIN Question Q on L.questionIdx = Q.questionIdx\n" +
                "INNER JOIN User U on L.userIdx = U.userIdx" +
                " where L.questionIdx = ? AND L.userIdx = ?\n";
        Object[] nContentParams = new Object[]{postReplyLikeReq.getReplyIdx(), postReplyLikeReq.getUserIdx()};
        //유저이름, 타이틀 저장
        List<Map<String,Object>> nContentParam2 = this.jdbcTemplate.queryForList(nContentQuery,
                nContentParams);


        String nContent = new String(nContentParam2.get(0).get("name")+" 님이 회원님의 "+
                "답변을 좋아합니다");



        Object[] creLikeNotParams = new Object[]{5, postReplyLikeReq.getReplyIdx(), postReplyLikeReq.getUserIdx(),
                nContent};

        this.jdbcTemplate.update(creLikNotQuery,creLikeNotParams);
        return nContent;

    }



}
