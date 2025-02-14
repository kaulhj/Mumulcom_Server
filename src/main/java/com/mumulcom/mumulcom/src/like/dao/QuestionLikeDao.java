package com.mumulcom.mumulcom.src.like.dao;


import com.mumulcom.mumulcom.src.like.dto.PostLikeRes;
import com.mumulcom.mumulcom.src.like.dto.PostQueLikeReq;

import com.mumulcom.mumulcom.src.like.dto.PostReplyLikeReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
//25
    public Long getTargetUserIdx(long questionIdx) {
        return this.jdbcTemplate.queryForObject("select exists (select q.userIdx\n" +
                "from Question q\n" +
                "inner join User U on q.userIdx = U.userIdx\n" +
                "where questionIdx = ?)", Long.class, questionIdx);

    }

//25.
    @Transactional(rollbackFor = Exception.class)
    public PostLikeRes createQuestionLike(PostQueLikeReq postQueLikeReq, int status){
        Object[] createQueLikeParams = new Object[]{postQueLikeReq.getQuestionIdx(), postQueLikeReq.getUserIdx()};
        String result;
        Long targetUserIdx = this.jdbcTemplate.queryForObject("select q.userIdx\n" +
                "from Question q\n" +
                "inner join User U on q.userIdx = U.userIdx\n" +
                "where questionIdx = ?", Long.class,postQueLikeReq.getQuestionIdx());
        //타겟 유저인덱스 추출(알림용)
        switch(status) {
            case 1: //최초질문 좋아요
                    String createQueLikeQuery = "INSERT INTO QuestionLike(QUESTIONIDX, USERIDX) VALUES (?, ?)";
                    this.jdbcTemplate.update(createQueLikeQuery, createQueLikeParams);
                    result = new String("해당 글을 좋아요 하였습니다.");
                    String creLikNotQuery = "INSERT INTO Notice (noticeCategoryIdx,questionIdx,userIdx,noticeContent)\n" +
                            "VALUES (?,?,?,?)";

                    int noticeCategory; //알림 카테고리 구하기
                try {
                    String nNecQuery = "select userIdx\n" +
                            "from Question\n" +
                            "where questionIdx=?";

                    Long uId = this.jdbcTemplate.queryForObject(nNecQuery, Long.class, postQueLikeReq.getQuestionIdx());

                    if(uId == postQueLikeReq.getUserIdx())
                        throw new IllegalArgumentException("자신의 질문을 좋아요 하였습니다.");
                }catch (IllegalArgumentException e){
                    return new PostLikeRes(targetUserIdx,e.getMessage());
                }



                String nContentQuery = "SELECT U.nickname,Q.title\n" +
                        "FROM `QuestionLike` L\n" +
                        "INNER JOIN Question Q on L.questionIdx = Q.questionIdx\n" +
                        "INNER JOIN User U on L.userIdx = U.userIdx" +
                        " where L.questionIdx = ? AND L.userIdx = ?\n";
                Object[] nContentParams = new Object[]{postQueLikeReq.getQuestionIdx(), postQueLikeReq.getUserIdx()};
                //유저이름, 타이틀 저장
                List<Map<String,Object>> nContentParam2 = this.jdbcTemplate.queryForList(nContentQuery,
                        nContentParams);


                String nContent = new String(nContentParam2.get(0).get("nickname")+" 님이 회원님의 "+
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

//26.5
    public Long replyNotTarInd(Long questionIdx){
        return this.jdbcTemplate.queryForObject("SELECT exists (select R.userIdx\n" +
                "            FROM Reply R\n" +
                "INNER JOIN User U on R.userIdx = U.userIdx\n" +
                "                WHERE replyIdx\n" +
                "  = ?)", long.class,questionIdx);
    }


    //26.
    @Transactional(rollbackFor = Exception.class)
    public PostLikeRes createReplyLike(PostReplyLikeReq postReplyLikeReq, int status){

            Object[] creatRepLikeParams = new Object[]{postReplyLikeReq.getReplyIdx(),postReplyLikeReq.getUserIdx()};
            Long noticeTargetUserIdx = this.jdbcTemplate.queryForObject("select R.userIdx\n" +
                    "            FROM Reply R\n" +
                    "INNER JOIN User U on R.userIdx = U.userIdx\n" +
                    "                WHERE replyIdx\n" +
                    "  = ?", Long.class,postReplyLikeReq.getReplyIdx());
            switch (status) {
                case 1:

                    String createLikeQuery = "INSERT INTO ReplyLike(ReplyIdx, USERIDX) VALUES (?, ?)";
                    Object[] createLikeParams = new Object[]{postReplyLikeReq.getReplyIdx(),
                           postReplyLikeReq.getUserIdx()};
                    this.jdbcTemplate.update(createLikeQuery, createLikeParams);

                    String creLikNotQuery = "INSERT INTO Notice (noticeCategoryIdx,questionIdx,userIdx,noticeContent)\n" +
                            "VALUES (?,?,?,?)";

                    int noticeCategory; //알림 카테고리 구하기
                    try {
                        String nNecQuery = "select userIdx\n" +
                                "from Reply\n" +
                                "where ReplyIdx=?";

                        Long uId = this.jdbcTemplate.queryForObject(nNecQuery, Long.class, postReplyLikeReq.getReplyIdx());

                        if(uId == postReplyLikeReq.getUserIdx())
                            throw new IllegalArgumentException("자신의 답변을 좋아요 하였습니다.");
                    }catch (IllegalArgumentException e){
                        return new PostLikeRes(noticeTargetUserIdx,e.getMessage());
                    }



                    //
                    String nContentQuery = "select u.nickname\n" +
                            "                    from User u\n" +
                            "                    where userIdx = ? ";

                    //유저이름, 타이틀 저장
                    String nickname = this.jdbcTemplate.queryForObject(nContentQuery,String.class,
                            postReplyLikeReq.getUserIdx());



                    String nContent = new String(nickname + " 님이 회원님의 " +
                            "답변을 좋아합니다");

                    long notQuestionidx = this.jdbcTemplate.queryForObject("select questionIdx from Reply where " +
                            "replyIdx = ?",long.class,postReplyLikeReq.getReplyIdx());
                    Object[] creLikeNotParams = new Object[]{5, notQuestionidx, noticeTargetUserIdx,
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
                "FROM QuestionLike ql\n" +
                "right join Question q on ql.questionIdx = q.questionIdx";

        Object[] getLikeStatParams = new Object[]{postQueLikeReq.getUserIdx(),postQueLikeReq.getQuestionIdx(),
        postQueLikeReq.getUserIdx(), postQueLikeReq.getQuestionIdx()};
        return this.jdbcTemplate.queryForObject(getLikeStatQuery,int.class,getLikeStatParams);


    }

    //26.4
    public int getReLikeStatus(PostReplyLikeReq postReplyLikeReq) {
        String getLikeStatQuery = "SELECT\n" +
                "        distinct CASE\n" +
                "            WHEN EXISTS(SELECT userIdx, replyIdx from ReplyLike where userIdx = ? AND replyIdx = ? AND status = 'active')= '1' then 2\n" +
                "            WHEN EXISTS(SELECT userIdx, replyIdx from ReplyLike where userIdx = ? AND replyIdx = ? AND status = 'inactive')= '1' then 3\n" +
                "            ELSE 1 END\n" +
                " FROM ReplyLike\n" +
                "right join Reply R on ReplyLike.replyIdx = R.replyIdx";

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