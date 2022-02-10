package com.mumulcom.mumulcom.src.reply.dao;

import com.mumulcom.mumulcom.src.reply.domain.MyReplyListRes;
import com.mumulcom.mumulcom.src.reply.domain.ReplyInfoRes;


import com.mumulcom.mumulcom.src.reply.dto.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class ReplyDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ReplyDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * yeji 19번 API
     * 답변 생성 + 알림
     */
    public PostReplyRes creatReply(List<String> imgUrls, PostReplyReq postReplyReq) {

        String replyImgResult;
        String createReplyQuery;
        Object[] createReplyParams;

        // Reply table insert
        if(postReplyReq.getReplyUrl() != null) {
            createReplyQuery = "insert into Reply(questionIdx, userIdx, replyUrl, content) values (?, ?, ?, ?)";
            createReplyParams = new Object[]{postReplyReq.getQuestionIdx(), postReplyReq.getUserIdx(), postReplyReq.getReplyUrl(), postReplyReq.getContent()};
        } else {
            createReplyQuery = "insert into Reply(questionIdx, userIdx, content) values (?, ?, ?)";
            createReplyParams = new Object[]{postReplyReq.getQuestionIdx(), postReplyReq.getUserIdx(), postReplyReq.getContent()};
        }

        this.jdbcTemplate.update(createReplyQuery, createReplyParams);

        // 마지막으로 삽입된 ReplyIdx 값 추출
        String lastInsertIdQuery = "select last_insert_id()";
        Long lastReplyIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery, Long.class);
        replyImgResult = null;

        // s3에서 받아온 url DB insert
        if(imgUrls.size() != 0) {
            String createReplyImgQuery = "insert into ReplyImage(replyIdx, url) value (?, ?)";
            for(String url : imgUrls) {
                Object[] createReplyImgParams = new Object[]{lastReplyIdx, url};
                this.jdbcTemplate.update(createReplyImgQuery, createReplyImgParams);
            }
            replyImgResult = "이미지 삽입이 완료됐습니다.";
        }

        /*
         ** s3 서버 사용하기 전 프론트에서 url 주소를 받아서 이미지 처리했을 때
         * if (postReplyReq.getUrl() != null) {

            // ReplyImg table insert
            String createReplyImgQuery = "insert into ReplyImage(lastReplyIdx, url) value (?, ?)";
            for (String url : postReplyReq.getUrl()) {
                Object[] createReplyImgParams = new Object[]{replyIdx, url};
                this.jdbcTemplate.update(createReplyImgQuery, createReplyImgParams);
            }
            replyImgResult = "이미지 삽입이 완료됐습니다.";
            }
         */

        // 알림 기능
        // 1. 질문 작성자에게 알림
        // 답변 생성한 유저 닉네임 추출
        String userNickname = this.jdbcTemplate.queryForObject("SELECT nickname FROM User WHERE userIdx = ?", String.class, postReplyReq.getUserIdx());
        // 질문한 유저 인덱스 추출 (알림 대상 유저)
        Long questionUserIdx = this.jdbcTemplate.queryForObject("SELECT userIdx FROM Question WHERE questionIdx = ?", Long.class, postReplyReq.getQuestionIdx());

        String createReplyNoticeQuery = "INSERT INTO Notice(noticeCategoryIdx, questionIdx, userIdx, noticeContent) values (1, ?, ?, " + "'" + userNickname + "님이 회원님의 질문에 답변을 달았습니다.')";
        Object[] createReplyNoticeParams = new Object[]{postReplyReq.getQuestionIdx(), questionUserIdx};
        this.jdbcTemplate.update(createReplyNoticeQuery, createReplyNoticeParams);
        String noticeReply = userNickname + "님이 회원님의 질문에 답변을 달았습니다.";

        // 2. 질문을 스크랩한 유저들에게 알림
        // 스크랩한 유저 인덱스 추출 (알림 대상 유저)
        List<Long> scrapUserIdxList = new ArrayList<>();
        this.jdbcTemplate.query("SELECT userIdx FROM Scrap WHERE questionIdx = ?",
                (rs, rowNum) -> scrapUserIdxList.add(
                        rs.getLong("userIdx")),
                postReplyReq.getQuestionIdx());

        String noticeScrap = null;
        if(scrapUserIdxList.isEmpty() == false) {
            String createReplyNoticeQuery2 = "INSERT INTO Notice(noticeCategoryIdx, questionIdx, userIdx, noticeContent) values (3, ?, ?, '회원님이 스크랩한 질문에 답변이 달렸습니다.')";

            for(Long userIdx : scrapUserIdxList) {
                Object[] createReplyNoticeParams2 = new Object[]{postReplyReq.getQuestionIdx(), userIdx};
                this.jdbcTemplate.update(createReplyNoticeQuery2, createReplyNoticeParams2);
            }
            noticeScrap = "회원님이 스크랩한 질문에 답변이 달렸습니다.";
        }

        return new PostReplyRes(lastReplyIdx, replyImgResult, noticeReply, questionUserIdx, noticeScrap, scrapUserIdxList);
    }

    /**
     * 휘정
     * 내가 답변한 코딩 질문 리스트 조회 API
     */
    public List<MyReplyListRes> myCodingReplyListResList(long userIdx, boolean isAdopted) {
        String myReplyListQuery;

        if(isAdopted == false) {
            myReplyListQuery = "select  questionList.questionIdx, questionList.profileImgUrl, nickname, bigCategoryName, smallCategoryName, title, DATE_FORMAT(questionList.createdAt, '%m-%d, %y') AS createdAt, likeCount, replyCount , r.content\n" +
                    "from\n" +
                    "(select q.questionIdx, nickname, bigCategoryName, smallCategoryName, title, q.createdAt, likeCount, replyCount , u.profileImgUrl\n" +
                    "from Question q, BigCategory b, SmallCategory s , User u, CodeQuestion c ,\n" +
                    "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
                    "from Question q left join (select questionIdx, count(*) likeCount from QuestionLike q join User u on q.userIdx = u.userIdx where u.status = 'Active' and q.status='active' group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
                    "(select q.questionIdx, ifnull(replyCount,0) as replyCount\n" +
                    "from Question q left join (select questionIdx, count(*) replyCount from Reply group by questionIdx) r on q.questionIdx = r.questionIdx) r\n" +
                    "where q.bigCategoryIdx = b.bigCategoryIdx and q.smallCategoryIdx = s.smallCategoryIdx and u.userIdx = q.userIdx and c.questionIdx = q.questionIdx \n" +
                    "and q.questionIdx = l.questionIdx and q.questionIdx = r.questionIdx) questionList join Reply r on questionList.questionIdx = r.questionIdx\n" +
                    "where r.userIdx = ? \n" +
                    "order by r.createdAt desc";
        } else { // 채택된 것만 보기
            myReplyListQuery = "select  questionList.questionIdx, questionList.profileImgUrl, nickname, bigCategoryName, smallCategoryName, title, DATE_FORMAT(questionList.createdAt, '%m-%d, %y') AS createdAt, likeCount, replyCount , r.content\n" +
                    "from\n" +
                    "(select q.questionIdx, nickname, bigCategoryName, smallCategoryName, title, q.createdAt, likeCount, replyCount , u.profileImgUrl\n" +
                    "from Question q, BigCategory b, SmallCategory s , User u, CodeQuestion c ,\n" +
                    "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
                    "from Question q left join (select questionIdx, count(*) likeCount from QuestionLike q join User u on q.userIdx = u.userIdx where u.status = 'Active' and q.status='active' group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
                    "(select q.questionIdx, ifnull(replyCount,0) as replyCount\n" +
                    "from Question q left join (select questionIdx, count(*) replyCount from Reply group by questionIdx) r on q.questionIdx = r.questionIdx) r\n" +
                    "where q.bigCategoryIdx = b.bigCategoryIdx and q.smallCategoryIdx = s.smallCategoryIdx and u.userIdx = q.userIdx and c.questionIdx = q.questionIdx \n" +
                    "and q.questionIdx = l.questionIdx and q.questionIdx = r.questionIdx) questionList join Reply r on questionList.questionIdx = r.questionIdx\n" +
                    "where r.userIdx = ? and r.status = \"adopted\" \n" +
                    "order by r.createdAt desc";
        }
        return this.jdbcTemplate.query(myReplyListQuery,
                (rs, rowNum) -> new MyReplyListRes(
                        rs.getLong("questionIdx"),
                        rs.getString("profileImgUrl"),
                        rs.getString("nickname"),
                        rs.getString("bigCategoryName"),
                        rs.getString("smallCategoryName"),
                        rs.getString("title"),
                        rs.getString("createdAt"),
                        rs.getInt("likeCount"),
                        rs.getInt("replyCount")
                ), userIdx);
    }

    /**
     * 휘정
     * 내가 답변한 개념 질문 리스트 조회 API
     */
    public List<MyReplyListRes> myConceptReplyListResList(long userIdx, boolean isAdopted) {
        String myReplyListQuery;

        if(isAdopted == false) {
            myReplyListQuery = "select  questionList.questionIdx, questionList.profileImgUrl, nickname, bigCategoryName, smallCategoryName, title,DATE_FORMAT(questionList.createdAt, '%m-%d, %y') AS createdAt, likeCount, replyCount , r.content\n" +
                    "from\n" +
                    "(select q.questionIdx,nickname, bigCategoryName, smallCategoryName, title, q.createdAt, likeCount, replyCount , u.profileImgUrl\n" +
                    "from Question q, BigCategory b, SmallCategory s , User u, ConceptQuestion c ,\n" +
                    "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
                    "from Question q left join (select questionIdx, count(*) likeCount from QuestionLike q join User u on q.userIdx = u.userIdx where u.status = 'Active' and q.status='active' group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
                    "(select q.questionIdx, ifnull(replyCount,0) as replyCount\n" +
                    "from Question q left join (select questionIdx, count(*) replyCount from Reply group by questionIdx) r on q.questionIdx = r.questionIdx) r\n" +
                    "where q.bigCategoryIdx = b.bigCategoryIdx and q.smallCategoryIdx = s.smallCategoryIdx and u.userIdx = q.userIdx and c.questionIdx = q.questionIdx \n" +
                    "and q.questionIdx = l.questionIdx and q.questionIdx = r.questionIdx) questionList join Reply r on questionList.questionIdx = r.questionIdx\n" +
                    "where r.userIdx = ?\n" +
                    "order by r.createdAt desc";
        } else { // 채택된 것만 보기
            myReplyListQuery = "select  questionList.questionIdx, questionList.profileImgUrl, nickname, bigCategoryName, smallCategoryName, title, DATE_FORMAT(questionList.createdAt, '%m-%d, %y') AS createdAt, likeCount, replyCount , r.content\n" +
                    "from\n" +
                    "(select q.questionIdx,nickname, bigCategoryName, smallCategoryName, title, q.createdAt, likeCount, replyCount , u.profileImgUrl\n" +
                    "from Question q, BigCategory b, SmallCategory s , User u, ConceptQuestion c ,\n" +
                    "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
                    "from Question q left join (select questionIdx, count(*) likeCount from QuestionLike q join User u on q.userIdx = u.userIdx where u.status = 'Active' and q.status='active' group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
                    "(select q.questionIdx, ifnull(replyCount,0) as replyCount\n" +
                    "from Question q left join (select questionIdx, count(*) replyCount from Reply group by questionIdx) r on q.questionIdx = r.questionIdx) r\n" +
                    "where q.bigCategoryIdx = b.bigCategoryIdx and q.smallCategoryIdx = s.smallCategoryIdx and u.userIdx = q.userIdx and c.questionIdx = q.questionIdx \n" +
                    "and q.questionIdx = l.questionIdx and q.questionIdx = r.questionIdx) questionList join Reply r on questionList.questionIdx = r.questionIdx\n" +
                    "where r.userIdx = ? and r.status = \"adopted\" \n" +
                    "order by r.createdAt desc";
        }

        return this.jdbcTemplate.query(myReplyListQuery,
                (rs, rowNum) -> new MyReplyListRes(
                        rs.getLong("questionIdx"),
                        rs.getString("profileImgUrl"),
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

    public ReplyInfoRes getReplyInfo(long replyIdx) {
        String getQuestionWriterQuery = "select r.userIdx as answerer, q.userIdx as writer, q.questionIdx\n" +
                "from Reply r join Question q on r.questionIdx = q.questionIdx\n" +
                "where replyIdx = ?";

        try{
            return this.jdbcTemplate.queryForObject(getQuestionWriterQuery,
                    (rs,rowNum) -> new ReplyInfoRes(
                            rs.getLong("writer"),
                            rs.getLong("answerer"),
                            rs.getLong("questionIdx")
                    )
                    ,replyIdx);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    /**
     * 휘정 채택하기 API
     * 채택된 답변의 status는 adopted
     */
    public int adoptReply(long replyIdx) {
        String adoptReplyQuery = "update Reply set status = 'adopted' , updatedAt = now() where replyIdx = ?";
        return this.jdbcTemplate.update(adoptReplyQuery, replyIdx); // 대응시켜 매핑시켜 쿼리 요청(생성했으면 1, 실패했으면 0)
    }

    //학준 29.

    @Transactional(rollbackFor = Exception.class)
    public PostReRepRes rereply(PostReReplReq postReReplReq) {
        Long noticeTargetUserIdx = this.jdbcTemplate.queryForObject("SELECT userIdx\n" +
                "FROM Reply\n" +
                "WHERE replyIdx = ?", long.class, postReReplReq.getReplyIdx());
        String RereplQuery = "INSERT INTO Rereply(replyIdx, userIdx, content, imageUrl )\n" +
                " VALUES (?, ?, ?, ?)";
        Object[] RereplParams = new Object[]{postReReplReq.getReplyIdx(), postReReplReq.getUserIdx(),
                postReReplReq.getContent(), postReReplReq.getImageUrl()};
        this.jdbcTemplate.update(RereplQuery, RereplParams);

        //questionIdx값 추출
        String questionIdxQuery = "SELECT DISTINCT questionIdx\n" +
                "FROM Reply RP\n" +
                "INNER JOIN Rereply R on RP.replyIdx = R.replyIdx\n" +
                "WHERE R.replyIdx = ?";
        int questionIdx = this.jdbcTemplate.queryForObject(questionIdxQuery,int.class,postReReplReq.getReplyIdx());
        String ReReplNotQuery = "INSERT INTO Notice(NOTICECATEGORYIDX, QUESTIONIDX, USERIDX, " +
                "NOTICECONTENT) VALUES (?, ?, ?, ?)";
        Object[] ReReplyNotParams = new Object[]{4, questionIdx, postReReplReq.getUserIdx(), new String("회원님이 답변한 글에 댓글이 달렸습니다.")};
        this.jdbcTemplate.update(ReReplNotQuery, ReReplyNotParams);
        return new PostReRepRes(noticeTargetUserIdx,"답변에 답글을 달았습니다");


    }

    //29.1
    public int reReplyAuth(PostReReplReq postReReplReq){
        String reRepAuthQuery = "SELECT userIdx from Reply where replyIdx = ?";
               long result =  this.jdbcTemplate.queryForObject(reRepAuthQuery,
                long.class,postReReplReq.getReplyIdx());
        if(result == postReReplReq.getUserIdx()){
            return 0;
        }else
            return 1;
    }

    //29.2
    public int repIdxExist(long replyIdx){
        String repIdxExistQuery = "SELECT EXISTS(SELECT replyIdx from Reply where\n " +
                " replyIdx =?)";
        return this.jdbcTemplate.queryForObject(repIdxExistQuery,int.class,replyIdx);
    }

    /**
     * yeji
     * 전체 답변 조회 API (명세서 22번)
     */
    public List<GetReplyRes> getReplyList(int questionIdx, int userIdx) {
        String getReplyListQuery =
                "SELECT r.replyIdx, r.questionIdx, r.userIdx, U.nickname, U.profileImgUrl, DATE_FORMAT(r.createdAt, '%m-%d, %y') AS createdAt, r.replyUrl AS replyUrl, r.content, img.url AS replyImgUrl, IFNULL(likeCount.lcount, 0) likeCount, IFNULL(reCount.rcount, 0) reReplyCount, CASE r.status WHEN 'active' THEN 'N' WHEN 'adopted' THEN 'Y' END AS status, IFNULL(il.isliked, 'N') AS isliked\n" +
                "FROM Reply r\n" +
                "INNER JOIN User U on r.userIdx = U.userIdx\n" +
                "LEFT JOIN (SELECT replyIdx, GROUP_CONCAT(url) url FROM ReplyImage GROUP BY replyIdx) img\n" +
                "on r.replyIdx = img.replyIdx\n" +
                "LEFT JOIN (SELECT replyIdx, count(replyIdx) AS lcount FROM ReplyLike where status = 'active' group by replyIdx) likeCount\n" +
                "ON r.replyIdx = likeCount.replyIdx\n" +
                "LEFT JOIN (SELECT replyIdx, count(replyIdx) AS rcount FROM Rereply group by replyIdx) reCount\n" +
                "on r.replyIdx = reCount.replyIdx\n" +
                "LEFT JOIN (SELECT replyIdx, CASE status WHEN 'active' THEN 'Y' WHEN 'inactive' THEN 'N' END AS isliked FROM ReplyLike WHERE userIdx = ?) il\n" +
                "ON r.replyIdx = il.replyIdx\n" +
                "WHERE r.questionIdx = ?\n" +
                "ORDER BY r.createdAt";

        try{
            return this.jdbcTemplate.query(getReplyListQuery,
                    (rs, rowNum) -> new GetReplyRes(
                            rs.getLong("replyIdx"),
                            rs.getLong("questionIdx"),
                            rs.getLong("userIdx"),
                            rs.getString("nickname"),
                            rs.getString("profileImgUrl"),
                            rs.getString("createdAt"),
                            rs.getString("replyUrl"),
                            rs.getString("content"),
                            Arrays.asList(rs.getString("replyImgUrl").split(",")),
                            rs.getInt("likeCount"),
                            rs.getInt("reReplyCount"),
                            rs.getString("status"),
                            rs.getString("isLiked")),
                    userIdx, questionIdx);
        } catch (NullPointerException nullPointerException) {
            return this.jdbcTemplate.query(getReplyListQuery,
                    (rs, rowNum) -> new GetReplyRes(
                            rs.getLong("replyIdx"),
                            rs.getLong("questionIdx"),
                            rs.getLong("userIdx"),
                            rs.getString("nickname"),
                            rs.getString("profileImgUrl"),
                            rs.getString("createdAt"),
                            rs.getString("replyUrl"),
                            rs.getString("content"),
                            Arrays.asList(),
                            rs.getInt("likeCount"),
                            rs.getInt("reReplyCount"),
                            rs.getString("status"),
                            rs.getString("isLiked")),
                    userIdx, questionIdx);
        }

    }

    /**
     * 휘정
     * 채택된 답변에 대한 알림 생성
     * "회원님이 한 답변이 채택되었습니다."
     * */
    public int addAdoptionNotice (ReplyInfoRes replyInfoRes, String content) {
        String adoptionNoticeQuery = "insert into Notice (NoticeCategoryIdx, questionIdx, userIdx, noticeContent) values (7,?,?,?)";
        Object[] adoptionNoticeParams = new Object[] {replyInfoRes.getQuestionIdx(), replyInfoRes.getAnswerer(), content};
        this.jdbcTemplate.update(adoptionNoticeQuery, adoptionNoticeParams);
        String lastInsertNoticeIdx = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertNoticeIdx, int.class); // 해당 쿼리문의 결과 마지막으로 삽인된 유저의 userIdx번호를 반환한다.

    }
}

