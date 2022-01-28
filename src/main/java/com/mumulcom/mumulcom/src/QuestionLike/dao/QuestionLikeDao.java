package com.mumulcom.mumulcom.src.QuestionLike.dao;


import com.mumulcom.mumulcom.src.QuestionLike.dto.PostLikeReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository

public class QuestionLikeDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String createLike(PostLikeReq postLikeReq){

            String createLikeQuery = "INSERT INTO `Like`(QUESTIONIDX, USERIDX,REPLYIDX) VALUES (?, ?,?)";
            Object[] createLikeParams = new Object[]{postLikeReq.getQuestionIdx(),
            postLikeReq.getUserIdx(),postLikeReq.getReplyIdx()};
            this.jdbcTemplate.update(createLikeQuery,createLikeParams);

            String creLikNotQuery = "INSERT INTO Notice (noticeCategoryIdx,questionIdx,userIdx,noticeContent)\n" +
                    "VALUES (?,?,?,?)";

            int noticeCategory; //알림 카테고리 구하기
             String lastContent = null;
        if(postLikeReq.getReplyIdx() == 0) {
                noticeCategory = 2;
                lastContent = new String(" 질문에 좋아요를 눌렀습니다.");
        }else {
            noticeCategory = 5;
            lastContent = new String(" 답변에 좋아요를 눌렀습니다.");
        }


        //
            String nContentQuery = "SELECT U.name,Q.title\n" +
                    "FROM `Like` L\n" +
                    "INNER JOIN Question Q on L.questionIdx = Q.questionIdx\n" +
                    "INNER JOIN User U on L.userIdx = U.userIdx" +
                    " where L.questionIdx = ? AND L.userIdx = ?\n" +
                    " AND L.replyIdx = ?";
            Object[] nContentParams = new Object[]{postLikeReq.getQuestionIdx(),postLikeReq.getUserIdx(),
            postLikeReq.getReplyIdx()};
        //유저이름, 타이틀 저장
        List<Map<String,Object>> nContentParam2 = this.jdbcTemplate.queryForList(nContentQuery,
                nContentParams);


        String nContent = new String(nContentParam2.get(0).get("name")+" 님이 회원님의 "+
                nContentParam2.get(0).get("title")+lastContent);



        Object[] creLikeNotParams = new Object[]{noticeCategory,postLikeReq.getQuestionIdx(),postLikeReq.getUserIdx(),
            nContent};

        this.jdbcTemplate.update(creLikNotQuery,creLikeNotParams);
            return nContent;

    }



}
