package com.mumulcom.mumulcom.src.question.dao;


import com.mumulcom.mumulcom.src.question.domain.MyQuestionListRes;
import com.mumulcom.mumulcom.src.question.domain.SearchCodingQuestionRes;
import com.mumulcom.mumulcom.src.question.domain.SearchConceptQuestionRes;

import com.mumulcom.mumulcom.src.question.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class QuestionDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //  9.1좋아요 데이터 갯수 구하기
    public int countSize(long userIdx) {
        String countQuery = "SELECT\n" +
                "    count(distinct Q.questionIdx )\n" +
                "    FROM\n" +
                "Question Q\n" +
                "LEFT JOIN `QuestionLike` L ON Q.questionIdx = L.questionIdx\n" +
                "where Q.userIdx = ? AND Q.status = 'active' \n" +
                "#group by Q.questionIdx\n" +
                "order by Q.questionIdx desc limit 4";
        return this.jdbcTemplate.queryForObject(countQuery, int.class, userIdx);
    }

    //9.
    @Transactional
    public List<GetRecQueRes> getRecQuestion(int countSize, long userIdx){


    /*

        //좋아요 값 배열에 넣기
        String getRecQueQuery =  "SELECT\n" +
                "       count(CASE WHEN Q.questionIdx = L.questionIdx and L.status = 'active' then 1 END )as LikeCount\n" +
                "FROM\n" +
                "Question Q\n" +
                "LEFT JOIN `QuestionLike` L ON Q.questionIdx = L.questionIdx\n" +
                "where Q.userIdx = ?  AND Q.status = 'active' \n" +
                "group by Q.questionIdx\n" +
                "order by Q.questionIdx desc limit 1 offset ?";

        List<Long> reply = new ArrayList<>();

        for(int i=0;i<countSize;i++) {
            Object[] getReqQueParams = new Object[]{userIdx, i};
            reply.add(this.jdbcTemplate.queryForObject(getRecQueQuery,
                    long.class,
                    getReqQueParams));
        }


        //배열 객체에 매핑
        String GetListQueQuery = "SELECT q.questionIdx,b.bigCategoryName,s.smallCategoryName,u.name,\n" +
                "              concat(MONTH(q.createdAt),'/',day(q.createdAt),',',substring(year(q.createdAt),-2))as created\n" +
                " ,count(CASE WHEN q.questionIdx = r.questionIdx and (r.status = 'active' or r.status = 'adopted') then 1 END )as replies\n" +
                ",title , u.profileImgUrl \n" +
                " FROM\n" +
                "\n" +
                "      Question q\n" +
                "          INNER JOIN User u on q.userIdx = u.userIdx\n" +
                "INNER JOIN BigCategory b on q.bigCategoryIdx = b.bigCategoryIdx\n" +
                "INNER JOIN SmallCategory s on q.smallCategoryIdx = s.smallCategoryIdx\n" +
                "\n" +
                "LEFT JOIN Reply r on q.questionIdx = r.questionIdx\n" +
                "\n" +
                "\n" +
                "where q.userIdx = ? AND q.status = 'active'\n" +
                "group by questionIdx\n" +
                "order by created desc limit 4";

     */
        String getLateListQuery = "SELECT q.questionIdx,b.bigCategoryName,s.smallCategoryName,u.nickname,\n" +
                "              concat(MONTH(q.createdAt),'/',day(q.createdAt),',',substring(year(q.createdAt),-2))as created\n" +
                ",title, u.profileImgUrl,ifnull(ql2.likeCount, 0) likeCount, ifnull(rc2.replyCount,0) replyCount\n" +
                " FROM\n" +
                "      Question q\n" +
                "INNER JOIN User u on q.userIdx = u.userIdx\n" +
                "INNER JOIN BigCategory b on q.bigCategoryIdx = b.bigCategoryIdx\n" +
                "INNER JOIN SmallCategory s on q.smallCategoryIdx = s.smallCategoryIdx\n" +
                "LEFT JOIN (SELECT questionIdx, count(CASE\n" +
                "        WHEN ql1.status = 'active' then 1 end) AS likeCount FROM QuestionLike ql1 group by questionIdx) AS ql2\n" +
                "                        ON q.questionIdx = ql2.questionIdx\n" +
                "LEFT JOIN (SELECT questionIdx, count(CASE\n" +
                "    WHEN rc1.status = 'active' then 1\n" +
                "    WHEN rc1.status = 'adopted' then 1 end) AS replyCount FROM Reply rc1 group by questionIdx) AS rc2\n" +
                "                        ON q.questionIdx = rc2.questionIdx\n" +
                "\n" +
                "  where u.userIdx = ?\n" +
                "order by q.createdAt desc limit 4";

        return this.jdbcTemplate.query(getLateListQuery,
                (rs, rowNum) -> new GetRecQueRes(
                        rowNum+1,
                        rs.getString("bigCategoryName"),
                        rs.getString("smallCategoryName"),
                        rs.getString("nickname"),
                        rs.getString("created"),
                        rs.getString("title"),
                        rs.getInt("replyCount"),
                        rs.getInt("likeCount"),
                        rs.getString("profileImgUrl")),

                userIdx);
    }


    //16-1 배열 사이즈 추출
    public int getRecQueSize(long userIdx){     //long 대문자
        String countQuery = "SELECT\n" +
                "    count(distinct Q.questionIdx)\n" +
                "    FROM\n" +
                "Question Q\n" +
                "INNER JOIN `QuestionLike` L ON Q.questionIdx = L.questionIdx\n" +
                "where Q.userIdx = ? and Q.status = 'active' \n" +
                "#group by Q.questionIdx\n" +
                "order by Q.questionIdx desc";
        return this.jdbcTemplate.queryForObject(countQuery,int.class,userIdx);
    }
    //16-2
    @Transactional
    public List<GetRecQueRes> getRecQuestions( long userIdx){



        /*
            //좋아요 값 배열에 넣기
        String getRecQueQuery =  "SELECT\n" +
                "       count(CASE WHEN Q.questionIdx = L.questionIdx and L.status = 'active' then 1 END )as LikeCount\n" +
                "FROM\n" +
                "Question Q\n" +
                "INNER JOIN `QuestionLike` L ON Q.questionIdx = L.questionIdx\n" +
                "where Q.userIdx = ? AND Q.status = 'active'\n" +
                "group by Q.questionIdx\n" +
                "order by Q.questionIdx desc limit 1 offset ?";

        List<Long> Likes = new ArrayList<>();

        for(int i=0;i<countSize;i++) {
            Object[] getReqQueParams = new Object[]{userIdx, i};
            Likes.add(this.jdbcTemplate.queryForObject(getRecQueQuery,
                    long.class,
                    getReqQueParams));
        }

        //
        //배열 객체에 매핑
        String GetListQueQuery = "SELECT q.questionIdx,b.bigCategoryName,s.smallCategoryName,u.name,\n" +
        "              concat(MONTH(q.createdAt),'/',day(q.createdAt),',',substring(year(q.createdAt),-2))as created\n" +
                " ,count(CASE WHEN q.questionIdx = r.questionIdx AND (r.status = 'active' or r.status = 'adopted')then 1 END )as replies\n" +
                ",title, u.profileImgUrl \n" +
                " FROM\n" +
                "\n" +
                "      Question q\n" +
                "          INNER JOIN User u on q.userIdx = u.userIdx\n" +
                "INNER JOIN BigCategory b on q.bigCategoryIdx = b.bigCategoryIdx\n" +
                "INNER JOIN SmallCategory s on q.smallCategoryIdx = s.smallCategoryIdx\n" +
                "\n" +
                "INNER JOIN Reply r on q.questionIdx = r.questionIdx\n" +
                "\n" +
                "\n" +
                "where q.userIdx = ? AND q.status = 'active'\n" +
                "group by questionIdx\n" +
                "order by created desc";

         */
        String getRepListQuery = "SELECT q.questionIdx,b.bigCategoryName,s.smallCategoryName,u.nickname,\n" +
                "              concat(MONTH(q.createdAt),'/',day(q.createdAt),',',substring(year(q.createdAt),-2))as created\n" +
                ",title, u.profileImgUrl,ifnull(ql2.likeCount, 0) likeCount, ifnull(rc2.replyCount,0) replyCount\n" +
                " FROM\n" +
                "      Question q\n" +
                "INNER JOIN User u on q.userIdx = u.userIdx\n" +
                "INNER JOIN BigCategory b on q.bigCategoryIdx = b.bigCategoryIdx\n" +
                "INNER JOIN SmallCategory s on q.smallCategoryIdx = s.smallCategoryIdx\n" +
                "LEFT JOIN (SELECT questionIdx, count(CASE\n" +
                "        WHEN ql1.status = 'active' then 1 end) AS likeCount FROM QuestionLike ql1 group by questionIdx) AS ql2\n" +
                "                        ON q.questionIdx = ql2.questionIdx\n" +
                "INNER JOIN (SELECT questionIdx, count(CASE\n" +
                "    WHEN rc1.status = 'active' then 1\n" +
                "    WHEN rc1.status = 'adopted' then 1 end) AS replyCount FROM Reply rc1 group by questionIdx) AS rc2\n" +
                "                        ON q.questionIdx = rc2.questionIdx\n" +
                "\n" +
                "  where u.userIdx = ?\n" +
                "order by q.createdAt desc";

        return this.jdbcTemplate.query(getRepListQuery,
                (rs, rowNum) -> new GetRecQueRes(
                        rowNum+1,
                        rs.getString("bigCategoryName"),
                        rs.getString("smallCategoryName"),
                        rs.getString("nickname"),
                        rs.getString("created"),
                        rs.getString("title"),
                        rs.getInt("replyCount"),
                        rs.getInt("likeCount"),
                        rs.getString("profileImgUrl")),
                userIdx);
    }



    //7. 코딩질문하기

    public String codeQuestion(List<String> imgUrls, CodeQuestionReq codeQuestionReq)  {
        //Question 테이블에 데이터 주입
        String InsertQueQuery = "INSERT INTO Question(userIdx,bigCategoryIdx,\n" +
                "                     smallCategoryIdx,title )\n" +
                "                     VALUES (?,?,?,?)";
        Object[] InsertQueParams = new Object[]{codeQuestionReq.getUserIdx(), codeQuestionReq.getBigCategoryIdx(),
                codeQuestionReq.getSmallCategoryIdx(), codeQuestionReq.getTitle()};
        this.jdbcTemplate.update(InsertQueQuery, InsertQueParams);


        //마지막 퀘스천인덱스 값 가져오기
        String lastQueIdQuery = " SELECT questionIdx\n" +
                " FROM\n" +
                "     Question\n" +
                " order by questionIdx desc limit 1 ";
        //int lastQueIdParams = 1;
        long lastQueId = this.jdbcTemplate.queryForObject(lastQueIdQuery,
                long.class
        );


        //코딩질문 테이블에 데이터 주입
        String CodQueTabQue = "INSERT INTO CodeQuestion(questionIdx, currentError,myCodingSkill,codeQuestionUrl)\n" +
                "VALUES (?,?,?,?)";
        Object[] CodQueTabParams = new Object[]{lastQueId, codeQuestionReq.getCurrentError(),
                codeQuestionReq.getMyCodingSkill(), codeQuestionReq.getCodeQuestionUrl()};

        this.jdbcTemplate.update(CodQueTabQue, CodQueTabParams);


        if (imgUrls.size() != 0) {
            //이미지에 넣기
            String InsImgTabQuery = "INSERT INTO Image(questionIdx, imageUrl) VALUES (?, ?)";
            for (String img : imgUrls) {
                Object[] InsImgTabParams = new Object[]{lastQueId, img};
                this.jdbcTemplate.update(InsImgTabQuery, InsImgTabParams);
            }

        }
        return new String("코딩질문이 등록되었습니다");
    }

    //7.2 학준
    public int checkUserStatus(long userIdx){
        String checkUStaQuery = "SELECT count(\n" +
                "    CASE\n" +
                "        WHEN u.status = 'active' then 1 end\n" +
                "           )\n" +
                "FROM User u\n" +
                "where userIdx = ?";
        return this.jdbcTemplate.queryForObject(checkUStaQuery,int.class,userIdx);
    }

    //이미지 업을경우 오버로딩


    //8.개념질문
    @Transactional
    public String conceptQuestion(List<String> imgUrls, ConceptQueReq conceptQueReq) {

        //큰질문 테이블에 값 넣기
        String InsertQueQuery = "INSERT INTO Question(userIdx,bigCategoryIdx,\n" +
                "                     smallCategoryIdx,title)\n" +
                "                     VALUES (?,?,?,?)";
        Object[] InsertQueParams = new Object[]{conceptQueReq.getUserIdx(), conceptQueReq.getBigCategoryIdx(),
                conceptQueReq.getSmallCategoryIdx(), conceptQueReq.getTitle()};
        this.jdbcTemplate.update(InsertQueQuery, InsertQueParams);

        //마지막 퀘스천 인덱스 추출
        String lastQueIdQuery = " SELECT questionIdx\n" +
                " FROM\n" +
                "     Question\n" +
                " order by questionIdx desc limit 1 ";
        //int lastQueIdParams = 1;
        long lastQueId = this.jdbcTemplate.queryForObject(lastQueIdQuery,
                long.class
        );

        //개념질문 테이블에 값 넣기기
        String ConceptQueTabQue = "INSERT INTO ConceptQuestion(questionIdx, content)\n" +
                "VALUES (?,?)";
        Object[] CodQueTabParams = new Object[]{lastQueId, conceptQueReq.getContent()
        };

        this.jdbcTemplate.update(ConceptQueTabQue, CodQueTabParams);

        //String InsImgTabQuery = "INSERT INTO Image(questionIdx, imageUrl) VALUES (?, ?)";
        //Object[] InsImgTabParams = new Object[]{lastQueId, codeQuestionReq.getImageUrls()};

        String InsImgTabQuery = "INSERT INTO Image(questionIdx, imageUrl) VALUES (?, ?)";
        for (String img : imgUrls) {
            Object[] InsImgTabParams = new Object[]{lastQueId, img};
            this.jdbcTemplate.update(InsImgTabQuery, InsImgTabParams);
        }
        return new String("개념질문이 등록되었습니다");
    }
    
    /**
     * yeji 10번 API
     * 코딩 질문 조회
     */
    public List<GetCodingQuestionRes> getCodingQuestions(int questionIdx) {
        String getCodingQuestionQuery =
                "SELECT q.questionIdx, u.userIdx, u.nickname, u.profileImgUrl,DATE_FORMAT(q.createdAt, '%m-%d, %y') AS createdAt, q.title, IFNULL(I.url,'') AS questionImgUrl, CQ.currentError, CQ.myCodingSkill, BC.bigCategoryName, SC.smallCategoryName, ifnull(l.likeCount, 0) likeCount, ifnull(r.replyCount, 0) replyCount\n" +
                        "FROM User u\n" +
                        "INNER JOIN Question q\n" +
                        "on u.userIdx = q.userIdx\n" +
                        "INNER JOIN CodeQuestion CQ on q.questionIdx = CQ.questionIdx\n" +
                        "INNER JOIN BigCategory BC on q.bigCategoryIdx = BC.bigCategoryIdx\n" +
                        "INNER JOIN SmallCategory SC on q.smallCategoryIdx = SC.smallCategoryIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, GROUP_CONCAT(imageUrl) url FROM Image GROUP BY questionIdx) I\n" +
                        "on q.questionIdx = I.questionIdx\n"+
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS likeCount FROM `QuestionLike` WHERE questionIdx = ? and status = 'active') l\n" +
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
                        rs.getString("profileImgUrl"),
                        rs.getString("createdAt"),
                        rs.getString("title"),
                        Arrays.asList(rs.getString("questionImgUrl").split(",")),
                        rs.getString("currentError"),
                        rs.getString("myCodingSkill"),
                        rs.getString("bigCategoryName"),
                        rs.getString("smallCategoryName"),
                        rs.getInt("likeCount"),
                        rs.getInt("replyCount")),
                getCodingQuestionParams, getCodingQuestionParams, getCodingQuestionParams);
    }

    /**
     * yeji 11번 API
     * 개념 질문 조회
     */
    public List<GetConceptQuestionRes> getConceptQuestions(int questionIdx) {
        String getConceptQuestionQuery =
                "SELECT q.questionIdx, u.userIdx, u.nickname, u.profileImgUrl,DATE_FORMAT(q.createdAt, '%m-%d, %y') AS createdAt, q.title, IFNULL(I.url,'') AS questionImgUrl, CQ.content, BC.bigCategoryName, SC.smallCategoryName, ifnull(l.likeCount, 0) likeCount, ifnull(r.replyCount, 0) replyCount\n" +
                        "FROM User u\n" +
                        "INNER JOIN Question q\n" +
                        "on u.userIdx = q.userIdx\n" +
                        "INNER JOIN ConceptQuestion CQ on q.questionIdx = CQ.questionIdx\n" +
                        "INNER JOIN BigCategory BC on q.bigCategoryIdx = BC.bigCategoryIdx\n" +
                        "INNER JOIN SmallCategory SC on q.smallCategoryIdx = SC.smallCategoryIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, GROUP_CONCAT(imageUrl) url FROM Image GROUP BY questionIdx) I\n" +
                        "on q.questionIdx = I.questionIdx\n"+
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS likeCount FROM `QuestionLike` WHERE questionIdx = ? and status = 'active') l\n" +
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
                        rs.getString("profileImgUrl"),
                        rs.getString("createdAt"),
                        rs.getString("title"),
                        Arrays.asList(rs.getString("questionImgUrl").split(",")),
                        rs.getString("content"),
                        rs.getString("bigCategoryName"),
                        rs.getString("smallCategoryName"),
                        rs.getInt("likeCount"),
                        rs.getInt("replyCount")),
                getConceptQuestionParams, getConceptQuestionParams, getConceptQuestionParams);
    }

    /**
     * yeji 12번 API
     * 카테고리별 질문 목록 조회 API (최신순, 핫한순 정렬)
     */
    public List<GetQuestionListRes> getQuestionsByCategory(int type, int sort, int bigCategoryIdx, int smallCategoryIdx, boolean isReplied, int lastQuestionIdx, int perPage) {
        String getQuestionsQuery;
        String orderBy = "";
        String typeSql = "";
        Object[] getQuestionsParams;

        if (sort == 1) {
            orderBy = "q.createdAt";
        } else if(sort == 2){
            orderBy = "likeCount";
        }

        if(type == 1) {
            typeSql = "INNER JOIN CodeQuestion CQ on q.questionIdx = CQ.questionIdx\n";
        } else if(type == 2) {
            typeSql = "INNER JOIN ConceptQuestion CQ on q.questionIdx = CQ.questionIdx\n";
        }

        if(smallCategoryIdx == 0) {
            if(isReplied == true) {
                getQuestionsQuery = "SELECT q.questionIdx, u.userIdx, u.nickname, u.profileImgUrl, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS createdAt, q.title, BC.bigCategoryName, SC.smallCategoryName, ifnull(l.likeCount, 0) likeCount, ifnull(r.replyCount, 0) replyCount\n" +
                        "FROM User u\n" +
                        "INNER JOIN Question q\n" +
                        "on u.userIdx = q.userIdx\n" +
                        typeSql +
                        "INNER JOIN BigCategory BC on q.bigCategoryIdx = BC.bigCategoryIdx\n" +
                        "INNER JOIN SmallCategory SC on q.smallCategoryIdx = SC.smallCategoryIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS likeCount FROM `QuestionLike` where status = 'active' group by questionIdx) l\n" +
                        "ON q.questionIdx = l.questionIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS replyCount FROM Reply group by questionIdx) r\n" +
                        "ON q.questionIdx = r.questionIdx\n" +
                        "where q.bigCategoryIdx = ?\n and exists(select * from Reply r where(r.questionIdx=q.questionIdx))" +
                        "order by "+ orderBy +" desc\n" +
                        "limit ?, ?";
            } else {
                getQuestionsQuery = "SELECT q.questionIdx, u.userIdx, u.nickname, u.profileImgUrl, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS createdAt, q.title, BC.bigCategoryName, SC.smallCategoryName, ifnull(l.likeCount, 0) likeCount, ifnull(r.replyCount, 0) replyCount\n" +
                        "FROM User u\n" +
                        "INNER JOIN Question q\n" +
                        "on u.userIdx = q.userIdx\n" +
                        typeSql +
                        "INNER JOIN BigCategory BC on q.bigCategoryIdx = BC.bigCategoryIdx\n" +
                        "INNER JOIN SmallCategory SC on q.smallCategoryIdx = SC.smallCategoryIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS likeCount FROM `QuestionLike` where status = 'active' group by questionIdx) l\n" +
                        "ON q.questionIdx = l.questionIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS replyCount FROM Reply group by questionIdx) r\n" +
                        "ON q.questionIdx = r.questionIdx\n" +
                        "where q.bigCategoryIdx = ?\n" +
                        "order by "+ orderBy +" desc\n" +
                        "limit ?, ?";
            } getQuestionsParams = new Object[]{bigCategoryIdx, lastQuestionIdx, perPage};
        } else {
            if(isReplied == true) {
                getQuestionsQuery = "SELECT q.questionIdx, u.userIdx, u.nickname, u.profileImgUrl, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS createdAt, q.title, BC.bigCategoryName, SC.smallCategoryName, ifnull(l.likeCount, 0) likeCount, ifnull(r.replyCount, 0) replyCount\n" +
                        "FROM User u\n" +
                        "INNER JOIN Question q\n" +
                        "on u.userIdx = q.userIdx\n" +
                        typeSql +
                        "INNER JOIN BigCategory BC on q.bigCategoryIdx = BC.bigCategoryIdx\n" +
                        "INNER JOIN SmallCategory SC on q.smallCategoryIdx = SC.smallCategoryIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS likeCount FROM `QuestionLike` where status = 'active' group by questionIdx) l\n" +
                        "ON q.questionIdx = l.questionIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS replyCount FROM Reply group by questionIdx) r\n" +
                        "ON q.questionIdx = r.questionIdx\n" +
                        "where q.smallCategoryIdx = ?\n and exists(select * from Reply r where(r.questionIdx=q.questionIdx))" +
                        "order by "+ orderBy +" desc\n" +
                        "limit ?, ?";
            } else {
                getQuestionsQuery = "SELECT q.questionIdx, u.userIdx, u.nickname, u.profileImgUrl, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS createdAt, q.title, BC.bigCategoryName, SC.smallCategoryName, ifnull(l.likeCount, 0) likeCount, ifnull(r.replyCount, 0) replyCount\n" +
                        "FROM User u\n" +
                        "INNER JOIN Question q\n" +
                        "on u.userIdx = q.userIdx\n" +
                        typeSql +
                        "INNER JOIN BigCategory BC on q.bigCategoryIdx = BC.bigCategoryIdx\n" +
                        "INNER JOIN SmallCategory SC on q.smallCategoryIdx = SC.smallCategoryIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS likeCount FROM `QuestionLike` where status = 'active' group by questionIdx) l\n" +
                        "ON q.questionIdx = l.questionIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS replyCount FROM Reply group by questionIdx) r\n" +
                        "ON q.questionIdx = r.questionIdx\n" +
                        "where q.smallCategoryIdx = ?\n" +
                        "order by "+ orderBy +" desc\n" +
                        "limit ?, ?";
            } getQuestionsParams = new Object[]{smallCategoryIdx, lastQuestionIdx, perPage};
        }

        return this.jdbcTemplate.query(getQuestionsQuery,
                (rs, rowNum) -> new GetQuestionListRes(
                        rs.getLong("questionIdx"),
                        rs.getLong("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("profileImgUrl"),
                        rs.getString("createdAt"),
                        rs.getString("title"),
                        rs.getString("bigCategoryName"),
                        rs.getString("smallCategoryName"),
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

    /**
     * hwijeong
     * 개념 질문 검색 API
     * 입력받은 keyword를 이용해서 질문 받아오기
     * */
    public List<SearchConceptQuestionRes> searchConceptQuestionRes(String keyword) {
        String searchConceptQuestionQuery = "select * \n" +
                "from (select q.questionIdx, u.profileImgUrl, nickname, bigCategoryName, smallCategoryName, title, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS updatedAt, content, likeCount, replyCount\n" +
                "from Question q, BigCategory b, SmallCategory s , User u, ConceptQuestion c ,\n" +
                "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
                "from Question q left join (select questionIdx, count(*) likeCount from QuestionLike q join User u on q.userIdx = u.userIdx where u.status = 'Active' group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
                "(select q.questionIdx, ifnull(replyCount,0) as replyCount\n" +
                "from Question q left join (select questionIdx, count(*) replyCount from Reply group by questionIdx) r on q.questionIdx = r.questionIdx) r\n" +
                "where q.bigCategoryIdx = b.bigCategoryIdx and q.smallCategoryIdx = s.smallCategoryIdx and u.userIdx = q.userIdx and c.questionIdx = q.questionIdx \n" +
                "and q.questionIdx = l.questionIdx and q.questionIdx = r.questionIdx) as form\n" +
                "where form.title like ? OR form.content like ?" +
                "order by updatedAt desc";
        String keywordForm = "%" + keyword + "%";
        Object[] keyWordList = new Object[] {keywordForm, keywordForm};
        return this.jdbcTemplate.query(searchConceptQuestionQuery,
                (rs, rowNum) -> new SearchConceptQuestionRes(
                        rs.getLong("questionIdx"),
                        rs.getString("profileImgUrl"),
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

    public List<SearchConceptQuestionRes> searchConceptQuestionRes() {

        String searchConceptQuestionQuery = "select q.questionIdx,u.profileImgUrl, nickname, bigCategoryName, smallCategoryName, title, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS updatedAt, content, likeCount, replyCount\n" +
                "from Question q, BigCategory b, SmallCategory s , User u, ConceptQuestion c ,\n" +
                "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
                "from Question q left join (select questionIdx, count(*) likeCount from QuestionLike q join User u on q.userIdx = u.userIdx where u.status = 'Active' group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
                "(select q.questionIdx, ifnull(replyCount,0) as replyCount\n" +
                "from Question q left join (select questionIdx, count(*) replyCount from Reply group by questionIdx) r on q.questionIdx = r.questionIdx) r\n" +
                "where q.bigCategoryIdx = b.bigCategoryIdx and q.smallCategoryIdx = s.smallCategoryIdx and u.userIdx = q.userIdx and c.questionIdx = q.questionIdx \n" +
                "and q.questionIdx = l.questionIdx and q.questionIdx = r.questionIdx";

        return this.jdbcTemplate.query(searchConceptQuestionQuery,
                (rs, rowNum) -> new SearchConceptQuestionRes(
                        rs.getLong("questionIdx"),
                        rs.getString("profileImgUrl"),
                        rs.getString("nickname"),
                        rs.getString("bigCategoryName"),
                        rs.getString("smallCategoryName"),
                        rs.getString("title"),
                        rs.getString("updatedAt"),
                        rs.getString("content"),
                        rs.getInt("likeCount"),
                        rs.getInt("replyCount")
                ));
    }

    /**
     * hwijeong
     * 코딩 질문 검색 API
     * 입력받은 keyword를 이용해서 질문 받아오기
     * */
    public List<SearchCodingQuestionRes> searchCodingQuestionRes() {
        String searchCodingQuestionQuery = "select q.questionIdx, u.profileImgUrl, nickname, bigCategoryName, smallCategoryName, title, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS updatedAt, c.currentError, c.myCodingSkill, likeCount, replyCount \n" +
                "from Question q, BigCategory b, SmallCategory s , User u, CodeQuestion c ,\n" +
                "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
                "from Question q left join (select questionIdx, count(*) likeCount from QuestionLike q join User u on q.userIdx = u.userIdx where u.status = 'Active' group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
                "(select q.questionIdx, ifnull(replyCount,0) as replyCount\n" +
                "from Question q left join (select questionIdx, count(*) replyCount from Reply group by questionIdx) r on q.questionIdx = r.questionIdx) r\n" +
                "where q.bigCategoryIdx = b.bigCategoryIdx and q.smallCategoryIdx = s.smallCategoryIdx and u.userIdx = q.userIdx and c.questionIdx = q.questionIdx \n" +
                "and q.questionIdx = l.questionIdx and q.questionIdx = r.questionIdx";
        return this.jdbcTemplate.query(searchCodingQuestionQuery,
                (rs, rowNum) -> new SearchCodingQuestionRes(
                        rs.getLong("questionIdx"),
                        rs.getString("profileImgUrl"),
                        rs.getString("nickname"),
                        rs.getString("bigCategoryName"),
                        rs.getString("smallCategoryName"),
                        rs.getString("title"),
                        rs.getString("updatedAt"),
                        rs.getString("currentError"),
                        rs.getString("myCodingSkill"),
                        rs.getInt("likeCount"),
                        rs.getInt("replyCount")
                ));
    }
    public List<SearchCodingQuestionRes> searchCodingQuestionRes(String keyword) {
        String searchCodingQuestionQuery = "select *\n" +
                "from (select q.questionIdx, u.profileImgUrl,  nickname, bigCategoryName, smallCategoryName, title, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS updatedAt, c.currentError, c.myCodingSkill, likeCount, replyCount\n" +
                "from Question q, BigCategory b, SmallCategory s , User u, CodeQuestion c ,\n" +
                "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
                "from Question q left join (select questionIdx, count(*) likeCount from QuestionLike q join User u on q.userIdx = u.userIdx where u.status = 'Active' group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
                "(select q.questionIdx, ifnull(replyCount,0) as replyCount\n" +
                "from Question q left join (select questionIdx, count(*) replyCount from Reply group by questionIdx) r on q.questionIdx = r.questionIdx) r\n" +
                "where q.bigCategoryIdx = b.bigCategoryIdx and q.smallCategoryIdx = s.smallCategoryIdx and u.userIdx = q.userIdx and c.questionIdx = q.questionIdx \n" +
                "and q.questionIdx = l.questionIdx and q.questionIdx = r.questionIdx) as form\n" +
                "where form.title like ? OR form.currentError like ? OR form.myCodingSkill like ?\n" +
                "order by updatedAt desc";
        String keywordForm = "%" + keyword + "%";
        Object[] keyWordList = new Object[] {keywordForm, keywordForm, keywordForm};
        return this.jdbcTemplate.query(searchCodingQuestionQuery,
                (rs, rowNum) -> new SearchCodingQuestionRes(
                        rs.getLong("questionIdx"),
                        rs.getString("profileImgUrl"),
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

    /**
     * hwijeong
     * 내가 한 코딩 질문 조회
     * userIdx를 이용해서 사용자 정보 받은 후
     * 해당 사용자가 한 질문 목록 보여주기
     * */
    public List<MyQuestionListRes> myCodingQuestionListRes (long userIdx, boolean isReplied) {
        String myQuestionListQuery;
        if(isReplied == false) {
            myQuestionListQuery = "select * \n" +
                    "from\n" +
                    "(select u.userIdx, q.questionIdx, u.profileImgUrl, nickname, bigCategoryName, smallCategoryName, title, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS updatedAt, likeCount, replyCount\n" +
                    "from Question q, BigCategory b, SmallCategory s , User u, CodeQuestion c ,\n" +
                    "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
                    "from Question q left join (select questionIdx, count(*) likeCount from QuestionLike q join User u on q.userIdx = u.userIdx where u.status = 'Active' group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
                    "(select q.questionIdx, ifnull(replyCount,0) as replyCount\n" +
                    "from Question q left join (select questionIdx, count(*) replyCount from Reply group by questionIdx) r on q.questionIdx = r.questionIdx) r\n" +
                    "where q.bigCategoryIdx = b.bigCategoryIdx and q.smallCategoryIdx = s.smallCategoryIdx and u.userIdx = q.userIdx and c.questionIdx = q.questionIdx \n" +
                    "and q.questionIdx = l.questionIdx and q.questionIdx = r.questionIdx) questionList\n" +
                    "where userIdx =?\n" +
                    "order by updatedAt desc";
        }else {
            myQuestionListQuery = "select * \n" +
                    "from\n" +
                    "(select u.userIdx, q.questionIdx, u.profileImgUrl,nickname, bigCategoryName, smallCategoryName, title, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS updatedAt, likeCount, replyCount\n" +
                    "from Question q, BigCategory b, SmallCategory s , User u, CodeQuestion c ,\n" +
                    "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
                    "from Question q left join (select questionIdx, count(*) likeCount from QuestionLike q join User u on q.userIdx = u.userIdx where u.status = 'Active' group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
                    "(select q.questionIdx, ifnull(replyCount,0) as replyCount\n" +
                    "from Question q left join (select questionIdx, count(*) replyCount from Reply group by questionIdx) r on q.questionIdx = r.questionIdx) r\n" +
                    "where q.bigCategoryIdx = b.bigCategoryIdx and q.smallCategoryIdx = s.smallCategoryIdx and u.userIdx = q.userIdx and c.questionIdx = q.questionIdx \n" +
                    "and q.questionIdx = l.questionIdx and q.questionIdx = r.questionIdx) questionList\n" +
                    "where userIdx = ? and replyCount <> 0\n" +
                    "order by updatedAt desc";
        }
        return this.jdbcTemplate.query(myQuestionListQuery,
                (rs,rowNum) -> new MyQuestionListRes(
                        rs.getLong("userIdx"),
                        rs.getLong("questionIdx"),
                        rs.getString("profileImgUrl"),
                        rs.getString("nickname"),
                        rs.getString("bigCategoryName"),
                        rs.getString("smallCategoryName"),
                        rs.getString("title"),
                        rs.getString("updatedAt"),
                        rs.getInt("likeCount"),
                        rs.getInt("replyCount")
                ),userIdx);
    }

    /**
     * hwijeong
     * 내가 한 개념 질문 조회
     * userIdx를 이용해서 사용자 정보 받은 후
     * 해당 사용자가 한 질문 목록 보여주기
     * */
    public List<MyQuestionListRes> myConceptQuestionListRes (long userIdx, boolean isReplied) {
        String myQuestionListQuery;
        if(isReplied == false) {
            myQuestionListQuery = " select * \n" +
                    "from\n" +
                    "(select u.userIdx, q.questionIdx, u.profileImgUrl, nickname, bigCategoryName, smallCategoryName, title, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS updatedAt, likeCount, replyCount\n" +
                    "from Question q, BigCategory b, SmallCategory s , User u, ConceptQuestion c ,\n" +
                    "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
                    "from Question q left join (select questionIdx, count(*) likeCount from QuestionLike q join User u on q.userIdx = u.userIdx where u.status = 'Active' group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
                    "(select q.questionIdx, ifnull(replyCount,0) as replyCount\n" +
                    "from Question q left join (select questionIdx, count(*) replyCount from Reply group by questionIdx) r on q.questionIdx = r.questionIdx) r\n" +
                    "where q.bigCategoryIdx = b.bigCategoryIdx and q.smallCategoryIdx = s.smallCategoryIdx and u.userIdx = q.userIdx and c.questionIdx = q.questionIdx \n" +
                    "and q.questionIdx = l.questionIdx and q.questionIdx = r.questionIdx) questionList\n" +
                    "where userIdx =?\n" +
                    "order by updatedAt desc";
        }else {
            myQuestionListQuery = " select * \n" +
                    "from\n" +
                    "(select u.userIdx, q.questionIdx, u.profileImgUrl, nickname, bigCategoryName, smallCategoryName, title, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS updatedAt, likeCount, replyCount\n" +
                    "from Question q, BigCategory b, SmallCategory s , User u, ConceptQuestion c ,\n" +
                    "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
                    "from Question q left join (select questionIdx, count(*) likeCount from QuestionLike q join User u on q.userIdx = u.userIdx where u.status = 'Active' group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
                    "(select q.questionIdx, ifnull(replyCount,0) as replyCount\n" +
                    "from Question q left join (select questionIdx, count(*) replyCount from Reply group by questionIdx) r on q.questionIdx = r.questionIdx) r\n" +
                    "where q.bigCategoryIdx = b.bigCategoryIdx and q.smallCategoryIdx = s.smallCategoryIdx and u.userIdx = q.userIdx and c.questionIdx = q.questionIdx \n" +
                    "and q.questionIdx = l.questionIdx and q.questionIdx = r.questionIdx) questionList\n" +
                    "where userIdx = ? and replyCount <> 0\n" +
                    "order by updatedAt desc";
        }
        return this.jdbcTemplate.query(myQuestionListQuery,
                (rs,rowNum) -> new MyQuestionListRes(
                        rs.getLong("userIdx"),
                        rs.getLong("questionIdx"),
                        rs.getString("profileImgUrl"),
                        rs.getString("nickname"),
                        rs.getString("bigCategoryName"),
                        rs.getString("smallCategoryName"),
                        rs.getString("title"),
                        rs.getString("updatedAt"),
                        rs.getInt("likeCount"),
                        rs.getInt("replyCount")
                ),userIdx);
    }
}
