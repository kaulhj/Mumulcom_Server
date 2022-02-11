package com.mumulcom.mumulcom.src.like.dao;


import com.mumulcom.mumulcom.src.like.dto.PostLikeRes;
import com.mumulcom.mumulcom.src.like.dto.PostQueLikeReq;

import com.mumulcom.mumulcom.src.like.dto.PostReplyLikeReq;
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

//25.
    public PostLikeRes createQuestionLike(PostQueLikeReq postQueLikeReq, int status){
        Object[] createQueLikeParams = new Object[]{postQueLikeReq.getQuestionIdx(), postQueLikeReq.getUserIdx()};
        String result;
        Long targetUserIdx = this.jdbcTemplate.queryForObject("SELECT userIdx\n" +
                "FROM Question\n" +
                "where questionIdx = ?", long.class, postQueLikeReq.getQuestionIdx());  //타겟 유저인덱스 추출(알림용)
        switch(status) {
            case 1: //최초스크랩
                String createQueLikeQuery = "INSERT INTO QuestionLike(QUESTIONIDX, USERIDX) VALUES (?, ?)";
                this.jdbcTemplate.update(createQueLikeQuery,createQueLikeParams);
                result =  new String("해당 글을 좋아요 하였습니다.");
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



                Object[] creLikeNotParams = new Object[]{2, postQueLikeReq.getQuestionIdx(), targetUserIdx,
                        nContent};

                this.jdbcTemplate.update(creLikNotQuery,creLikeNotParams);

                return new PostLikeRes(targetUserIdx,nContent);
            case 2:
                String changeToInactiveQuery = "update QuestionLike\n" +
                        "SET status = 'inactive'\n" +
                        "where questionIdx = ? AND userIdx = ?";
                this.jdbcTemplate.update(changeToInactiveQuery,createQueLikeParams);
                return new PostLikeRes(new Long(0),"해당 글을 좋아요 취소하였습니다.");
            default: //3
                String changeToActiveQuery =  "update QuestionLike\n" +
                        "SET status = 'active'\n" +
                        "where questionIdx = ? AND userIdx = ?";
                this.jdbcTemplate.update(changeToActiveQuery,createQueLikeParams);
                return new PostLikeRes(new Long(0), "해당 글을 다시 좋아요 하였습니다.");

        }



    }


    //26.
    public PostLikeRes createReplyLike(PostReplyLikeReq postReplyLikeReq, int status){

            Object[] creatRepLikeParams = new Object[]{postReplyLikeReq.getReplyIdx(),postReplyLikeReq.getUserIdx()};
            switch (status) {
                case 1:
                    Long noticeTargetUserIdx = this.jdbcTemplate.queryForObject("SELECT userIdx\n" +
                            "FROM Reply\n" +
                            "WHERE replyIdx = ?", long.class,postReplyLikeReq.getReplyIdx());
                    String createLikeQuery = "INSERT INTO ReplyLike(ReplyIdx, USERIDX) VALUES (?, ?)";
                    Object[] createLikeParams = new Object[]{postReplyLikeReq.getReplyIdx(),
                           postReplyLikeReq.getUserIdx()};
                    this.jdbcTemplate.update(createLikeQuery, createLikeParams);

                    String creLikNotQuery = "INSERT INTO Notice (noticeCategoryIdx,questionIdx,userIdx,noticeContent)\n" +
                            "VALUES (?,?,?,?)";

                    int noticeCategory; //알림 카테고리 구하기
                    String lastContent = null;

                    lastContent = new String(" 답변에 좋아요를 눌렀습니다.");


                    //
                    String nContentQuery = "select u.nickname\n" +
                            "                    from User u\n" +
                            "                    where userIdx = ? ";

                    //유저이름, 타이틀 저장
                    String nickname = this.jdbcTemplate.queryForObject(nContentQuery,String.class,
                            postReplyLikeReq.getUserIdx());



                    String nContent = new String(nickname + " 님이 회원님의 " +
                            "답변을 좋아합니다");


                    Object[] creLikeNotParams = new Object[]{5, postReplyLikeReq.getReplyIdx(), noticeTargetUserIdx,
                            nContent};

                    this.jdbcTemplate.update(creLikNotQuery, creLikeNotParams);
                    return new PostLikeRes(noticeTargetUserIdx, nContent);
                case 2:
                    String changeToInactiveQuery = "update ReplyLike\n" +
                            "SET status = 'inactive'\n" +
                            "where ReplyIdx = ? AND userIdx = ?";
                    this.jdbcTemplate.update(changeToInactiveQuery,creatRepLikeParams);
                    return new PostLikeRes(new Long(0),"해당 답변 좋아요를 취소하였습니다.");
                default : //3
                    String changeToActiveQuery =  "update ReplyLike\n" +
                            "SET status = 'active'\n" +
                            "where ReplyIdx = ? AND userIdx = ?";
                    this.jdbcTemplate.update(changeToActiveQuery,creatRepLikeParams);
                    return new PostLikeRes(new Long(0),"해당 답변을 다시 좋아요 하였습니다.");

        }
    }

    //25.2
    public int LikeAuth(PostQueLikeReq postQueLikeReq){
        String queLikeAuthQuery = "select userIdx\n" +
                "from Question\n" +
                "where questionIdx = ?";
        if(postQueLikeReq.getUserIdx() == this.jdbcTemplate.queryForObject(queLikeAuthQuery, long.class,
                postQueLikeReq.getQuestionIdx())){
            return 0;
        }else
            return 1;
    }

    //25.1
    public int likeQueIdExist(Long getQuestionIdx){
        String checkLikeQueQuery = "SELECT EXISTS(SELECT questionIdx from Question where " +
                "questionIdx =?)";

        return this.jdbcTemplate.queryForObject(checkLikeQueQuery, int.class,
                getQuestionIdx);
    }

    //26.2 //답변인덱스 존재여부 확인
    public int likeReplyIdExist(Long getReplyIdx){
        String checkLikeReplQuery = "SELECT EXISTS(SELECT ReplyIdx from Reply where " +
                             "replyIdx = ?)";

                      return this.jdbcTemplate.queryForObject(checkLikeReplQuery, int.class,
                              getReplyIdx);
    }

    //25.3 질문좋아요 활성화 상태 검사
    public int checkUserStatus(PostQueLikeReq postQueLikeReq) {
        String checkUStaQuery = "SELECT count(\n" +
                "                  CASE\n" +
                "                       WHEN U.status = 'inactive' then 1 end\n" +
                "                          )\n" +
                "                FROM ReplyLike qu\n" +
                "                RIGHT JOIN User U ON qu.userIdx = U.userIdx\n" +
                "                where U.userIdx = ?";
        return this.jdbcTemplate.queryForObject(checkUStaQuery, int.class, postQueLikeReq.getUserIdx()
               );
    }

    //26.3
    public int checkUserStatus(PostReplyLikeReq postReplyLikeReq) {
        String checkUStaQuery = "SELECT count(\n" +
                "    CASE\n" +
                "        WHEN U.status = 'inactive' then 1 end\n" +
                "           )\n" +
                "FROM ReplyLike qu\n" +
                "INNER JOIN User U on qu.userIdx = U.userIdx" +
                " where qu.userIdx = ? and qu.replyIdx = ?";
        return this.jdbcTemplate.queryForObject(checkUStaQuery, int.class, postReplyLikeReq.getUserIdx(),
                postReplyLikeReq.getReplyIdx());
    }


    //25.4
    public int getLikeStatus(PostQueLikeReq postQueLikeReq){
        String getLikeStatQuery  = "SELECT\n" +
                "        distinct CASE\n" +
                "            WHEN EXISTS(SELECT userIdx, questionIdx from QuestionLike where userIdx = ? AND questionIdx = ? AND status = 'active')= '1' then 2\n" +
                "            WHEN EXISTS(SELECT userIdx, questionIdx from QuestionLike where userIdx = ? AND questionIdx = ? AND status = 'inactive')= '1' then 3\n" +
                "            ELSE 1 END\n" +
                "FROM QuestionLike";

        Object[] getLikeStatParams = new Object[]{postQueLikeReq.getUserIdx(),postQueLikeReq.getQuestionIdx(),
        postQueLikeReq.getUserIdx(), postQueLikeReq.getQuestionIdx()};
        return this.jdbcTemplate.queryForObject(getLikeStatQuery,int.class,getLikeStatParams);


    }

    //26.4
    public int getLikeStatus(PostReplyLikeReq postReplyLikeReq) {
        String getLikeStatQuery = "SELECT\n" +
                "        distinct CASE\n" +
                "            WHEN EXISTS(SELECT userIdx, replyIdx from ReplyLike where userIdx = ? AND replyIdx = ? AND status = 'active')= '1' then 2\n" +
                "            WHEN EXISTS(SELECT userIdx, replyIdx from ReplyLike where userIdx = ? AND replyIdx = ? AND status = 'inactive')= '1' then 3\n" +
                "            ELSE 1 END\n" +
                "FROM ReplyLike";

        Object[] getLikeStatParams = new Object[]{postReplyLikeReq.getUserIdx(), postReplyLikeReq.getReplyIdx(),
                postReplyLikeReq.getUserIdx(), postReplyLikeReq.getReplyIdx()};
        return this.jdbcTemplate.queryForObject(getLikeStatQuery, int.class, getLikeStatParams);

    }
    //26.2
    public int ReplyLikeAuth(PostReplyLikeReq postReplyLikeReq ){
        String queLikeAuthQuery = "select userIdx\n" +
                "from Reply\n" +
                "where replyIdx = ?";
        if(postReplyLikeReq.getUserIdx() == this.jdbcTemplate.queryForObject(queLikeAuthQuery, long.class,
                postReplyLikeReq.getReplyIdx()) ){
            return 0;
        }else
            return 1;
    }



}