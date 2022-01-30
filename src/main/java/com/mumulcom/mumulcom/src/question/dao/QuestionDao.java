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
import java.util.List;

@Repository
public class QuestionDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //2.
    @Transactional
    public GetRecQueRes getRecentQuestion(long userIdx){
        //like값 가져오기
        String getRecQueQuery1 = "SELECT\n" +
                "       count(CASE WHEN Q.questionIdx = L.questionIdx then 1 END )as LikeCount\n" +
                "FROM\n" +
                "Question Q\n" +
                "INNER JOIN `QuestionLike` L ON Q.questionIdx = L.questionIdx\n" +
                "where Q.userIdx = ?\n" +
                "group by Q.questionIdx\n" +
                "order by Q.questionIdx desc limit 1";
        long getRecQueParam = userIdx;
         long reply  = this.jdbcTemplate.queryForObject(getRecQueQuery1,
                long.class,
                getRecQueParam);

        //나머지 값들 조회
        String getRecQueQuery2 = "SELECT q.questionIdx,q.bigCategoryIdx,q.smallCategoryIdx,u.name,\n" +
                "              concat(MONTH(q.createdAt),'/',day(q.createdAt),',',substring(year(q.createdAt),-2))as created\n" +
                " ,count(CASE WHEN q.questionIdx = r.questionIdx then 1 END )as replies\n" +
                ",title \n" +
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
                "where q.userIdx = ?\n" +
                "group by questionIdx\n" +
                "order by created desc limit 1";
        return this.jdbcTemplate.queryForObject(getRecQueQuery2,
                (rs, rowNum) -> new GetRecQueRes(
                        rs.getLong("bigCategoryIdx"),
                        rs.getLong("smallCategoryIdx"),
                        rs.getString("name"),
                        rs.getString("created"),
                        rs.getLong("replies"),
                        rs.getString("title"),
                        reply
                ),
                getRecQueParam);

    }

    //21. 홈화면 최근질문 페이징조회
    public GetRecQueRes getRecQueByPage(long userIdx, int pages) {
        String getRecQueQuery1 = "SELECT\n" +
                "       count(CASE WHEN Q.questionIdx = L.questionIdx then 1 END )as LikeCount\n" +
                "FROM\n" +
                "Question Q\n" +
                "INNER JOIN `QuestionLike` L ON Q.questionIdx = L.questionIdx\n" +
                "where Q.userIdx = ?\n" +
                "group by Q.questionIdx\n" +
                "order by Q.questionIdx desc limit 1 offset ?";

        Object[] getRecQueParam = new Object[]{userIdx, pages};
        long reply = this.jdbcTemplate.queryForObject(getRecQueQuery1,
                long.class,
                getRecQueParam);

        //나머지 값들 조회
        String getRecQueQuery2 = "SELECT q.questionIdx,q.bigCategoryIdx,q.smallCategoryIdx,u.name,\n" +
                "              concat(MONTH(q.createdAt),'/',day(q.createdAt),',',substring(year(q.createdAt),-2))as created\n" +
                " ,count(CASE WHEN q.questionIdx = r.questionIdx then 1 END )as replies\n" +
                ",title \n" +
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
                "where q.userIdx = ?\n" +
                "group by questionIdx\n" +
                "order by created desc limit 1 offset ?";
        return this.jdbcTemplate.queryForObject(getRecQueQuery2,
                (rs, rowNum) -> new GetRecQueRes(
                        rs.getLong("bigCategoryIdx"),
                        rs.getLong("smallCategoryIdx"),
                        rs.getString("name"),
                        rs.getString("created"),
                        rs.getLong("replies"),
                        rs.getString("title"),
                        reply
                ),
                getRecQueParam);

    }


    //20.
    @Transactional
    public List<GetRecQueRes> getRecQuestions(long userIdx){

        String countQuery = "SELECT\n" +
                "    count(distinct Q.questionIdx)\n" +
                "    FROM\n" +
                "Question Q\n" +
                "INNER JOIN `QuestionLike` L ON Q.questionIdx = L.questionIdx\n" +
                "where Q.userIdx = ?\n" +
                "#group by Q.questionIdx\n" +
                "order by Q.questionIdx desc";
        int countSize = this.jdbcTemplate.queryForObject(countQuery,int.class,userIdx);


            //좋아요 값 배열에 넣기
        String getRecQueQuery =  "SELECT\n" +
                "       count(CASE WHEN Q.questionIdx = L.questionIdx then 1 END )as LikeCount\n" +
                "FROM\n" +
                "Question Q\n" +
                "INNER JOIN `QuestionLike` L ON Q.questionIdx = L.questionIdx\n" +
                "where Q.userIdx = ?\n" +
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
        String GetListQueQuery = "SELECT q.questionIdx,q.bigCategoryIdx,q.smallCategoryIdx,u.name,\n" +
        "              concat(MONTH(q.createdAt),'/',day(q.createdAt),',',substring(year(q.createdAt),-2))as created\n" +
                " ,count(CASE WHEN q.questionIdx = r.questionIdx then 1 END )as replies\n" +
                ",title \n" +
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
                "where q.userIdx = ?\n" +
                "group by questionIdx\n" +
                "order by created desc";
        return this.jdbcTemplate.query(GetListQueQuery,
                (rs, rowNum) -> new GetRecQueRes(
                        rs.getLong("bigCategoryIdx"),
                        rs.getLong("smallCategoryIdx"),
                        rs.getString("name"),
                        rs.getString("created"),
                        rs.getLong("replies"),
                        rs.getString("title"),
                        reply.get(rowNum)),
                userIdx);
    }



    //3. 코딩질문하기
    @Transactional
    public String codeQuestion(List<String> imgUrls, CodeQuestionReq codeQuestionReq) {

        String InsertQueQuery = "INSERT INTO Question(userIdx,bigCategoryIdx,\n" +
                "                     smallCategoryIdx,title)\n" +
                "                     VALUES (?,?,?,?)";
        Object[] InsertQueParams = new Object[]{codeQuestionReq.getUserIdx(), codeQuestionReq.getBigCategoryIdx(),
                codeQuestionReq.getSmallCategoryIdx(), codeQuestionReq.getTitle()};
        this.jdbcTemplate.update(InsertQueQuery, InsertQueParams);

        String lastQueIdQuery = " SELECT questionIdx\n" +
                " FROM\n" +
                "     Question\n" +
                " order by questionIdx desc limit 1 ";
        //int lastQueIdParams = 1;
        long lastQueId = this.jdbcTemplate.queryForObject(lastQueIdQuery,
                long.class
        );

        String CodQueTabQue = "INSERT INTO CodeQuestion(questionIdx, currentError,myCodingSkill)\n" +
                "VALUES (?,?,?)";
        Object[] CodQueTabParams = new Object[]{lastQueId, codeQuestionReq.getCurrentError(),
                codeQuestionReq.getMyCodingSkill()};

        this.jdbcTemplate.update(CodQueTabQue, CodQueTabParams);

        //String InsImgTabQuery = "INSERT INTO Image(questionIdx, imageUrl) VALUES (?, ?)";
        //Object[] InsImgTabParams = new Object[]{lastQueId, codeQuestionReq.getImageUrls()};

        //이미지에 넣기
        String InsImgTabQuery = "INSERT INTO Image(questionIdx, imageUrl) VALUES (?, ?)";
        for (String img : imgUrls) {
            Object[] InsImgTabParams = new Object[]{lastQueId, img};
            this.jdbcTemplate.update(InsImgTabQuery, InsImgTabParams);
        }
        return new String("코딩질문이 등록되었습니다");

        /*
        public int[] batchInsert(codeQuestionReq.getImageUrls() image)
        this.jdbcTemplate.batchUpdate(InsImgTabQuery,
                new BatchPreparedStatementSetter() {

                   // List<String> images = codeQuestionReq.getImageUrls();
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1,lastQueId);
                        ps.setString(2,images.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return images.size();
                    }
                });

         */


    }

    //4.개념질문
    @Transactional
    public String conceptQuestion(ConceptQueReq conceptQueReq) {




        String InsertQueQuery = "INSERT INTO Question(userIdx,bigCategoryIdx,\n" +
                "                     smallCategoryIdx,title)\n" +
                "                     VALUES (?,?,?,?)";
        Object[] InsertQueParams = new Object[]{conceptQueReq.getUserIdx(), conceptQueReq.getBigCategoryIdx(),
                conceptQueReq.getSmallCategoryIdx(), conceptQueReq.getTitle()};
        this.jdbcTemplate.update(InsertQueQuery, InsertQueParams);

        String lastQueIdQuery = " SELECT questionIdx\n" +
                " FROM\n" +
                "     Question\n" +
                " order by questionIdx desc limit 1 ";
        //int lastQueIdParams = 1;
        long lastQueId = this.jdbcTemplate.queryForObject(lastQueIdQuery,
                long.class
        );

        String CodQueTabQue = "INSERT INTO ConceptQuestion(questionIdx, content)\n" +
                "VALUES (?,?)";
        Object[] CodQueTabParams = new Object[]{lastQueId, conceptQueReq.getContent()
        };

        this.jdbcTemplate.update(CodQueTabQue, CodQueTabParams);

        //String InsImgTabQuery = "INSERT INTO Image(questionIdx, imageUrl) VALUES (?, ?)";
        //Object[] InsImgTabParams = new Object[]{lastQueId, codeQuestionReq.getImageUrls()};
        String InsImgTabQuery = "INSERT INTO Image(questionIdx, imageUrl) VALUES (?, ?)";
        for (int i = 0; i < conceptQueReq.getImageUrls().size(); i++) {
            Object[] InsImgTabParams = new Object[]{lastQueId, conceptQueReq.getImageUrls().get(i)};
            this.jdbcTemplate.update(InsImgTabQuery, InsImgTabParams);
        }
        return new String("개념질문이 등록되었습니다");
    }
    
    /**
     * yeji 5번 API
     * 코딩 질문 조회
     */
    public List<GetCodingQuestionRes> getCodingQuestions(int questionIdx) {
        String getCodingQuestionQuery =
                "SELECT q.questionIdx, u.userIdx, u.nickname, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS createdAt, q.title, CQ.currentError, CQ.myCodingSkill, q.bigCategoryIdx, q.smallCategoryIdx, ifnull(l.likeCount, 0) likeCount, ifnull(r.replyCount, 0) replyCount\n" +
                        "FROM User u\n" +
                        "INNER JOIN Question q\n" +
                        "on u.userIdx = q.userIdx\n" +
                        "INNER JOIN CodeQuestion CQ on q.questionIdx = CQ.questionIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS likeCount FROM `QuestionLike` WHERE questionIdx = ?) l\n" +
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
                        rs.getString("createdAt"),
                        rs.getString("title"),
                        rs.getString("currentError"),
                        rs.getString("myCodingSkill"),
                        rs.getString("bigCategoryIdx"),
                        rs.getString("smallCategoryIdx"),
                        rs.getInt("likeCount"),
                        rs.getInt("replyCount")),
                getCodingQuestionParams, getCodingQuestionParams, getCodingQuestionParams);
    }

    /**
     * yeji 6번 API
     * 개념 질문 조회
     */
    public List<GetConceptQuestionRes> getConceptQuestions(int questionIdx) {
        String getConceptQuestionQuery =
                "SELECT q.questionIdx, u.userIdx, u.nickname, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS createdAt, q.title, CQ.content, q.bigCategoryIdx, q.smallCategoryIdx, ifnull(l.likeCount, 0) likeCount, ifnull(r.replyCount, 0) replyCount\n" +
                        "FROM User u\n" +
                        "INNER JOIN Question q\n" +
                        "on u.userIdx = q.userIdx\n" +
                        "INNER JOIN ConceptQuestion CQ on q.questionIdx = CQ.questionIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS likeCount FROM `QuestionLike` WHERE questionIdx = ?) l\n" +
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
                        rs.getString("createdAt"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("bigCategoryIdx"),
                        rs.getString("smallCategoryIdx"),
                        rs.getInt("likeCount"),
                        rs.getInt("replyCount")),
                getConceptQuestionParams, getConceptQuestionParams, getConceptQuestionParams);
    }

    /**
     * yeji 7번 API
     * 카테고리별 질문 목록 조회 API (최신순, 핫한순 정렬)
     */
    public List<GetQuestionListRes> getQuestionsByCategory(int sort, int bigCategoryIdx, int smallCategoryIdx, boolean isReplied, int lastQuestionIdx, int perPage) {
        String getQuestionsQuery;
        String orderBy = "";
        Object[] getQuestionsParams;

        if (sort == 1) {
            orderBy = "q.createdAt";
        } else if(sort == 2){
            orderBy = "likeCount";
        }

        if(smallCategoryIdx == 0) {
            if(isReplied == true) {
                getQuestionsQuery = "SELECT q.questionIdx, u.userIdx, u.nickname, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS createdAt, q.title, q.bigCategoryIdx, q.smallCategoryIdx, ifnull(l.likeCount, 0) likeCount, ifnull(r.replyCount, 0) replyCount\n" +
                        "FROM User u\n" +
                        "INNER JOIN Question q\n" +
                        "on u.userIdx = q.userIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS likeCount FROM `QuestionLike` group by questionIdx) l\n" +
                        "ON q.questionIdx = l.questionIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS replyCount FROM Reply group by questionIdx) r\n" +
                        "ON q.questionIdx = r.questionIdx\n" +
                        "where q.bigCategoryIdx = ?\n and exists(select * from Reply r where(r.questionIdx=q.questionIdx))" +
                        "order by "+ orderBy +" desc\n" +
                        "limit ?, ?";
            } else {
                getQuestionsQuery = "SELECT q.questionIdx, u.userIdx, u.nickname, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS createdAt, q.title, q.bigCategoryIdx, q.smallCategoryIdx, ifnull(l.likeCount, 0) likeCount, ifnull(r.replyCount, 0) replyCount\n" +
                        "FROM User u\n" +
                        "INNER JOIN Question q\n" +
                        "on u.userIdx = q.userIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS likeCount FROM `QuestionLike` group by questionIdx) l\n" +
                        "ON q.questionIdx = l.questionIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS replyCount FROM Reply group by questionIdx) r\n" +
                        "ON q.questionIdx = r.questionIdx\n" +
                        "where q.bigCategoryIdx = ?\n" +
                        "order by "+ orderBy +" desc\n" +
                        "limit ?, ?";
            } getQuestionsParams = new Object[]{bigCategoryIdx, lastQuestionIdx, perPage};
        } else {
            if(isReplied == true) {
                getQuestionsQuery = "SELECT q.questionIdx, u.userIdx, u.nickname, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS createdAt, q.title, q.bigCategoryIdx, q.smallCategoryIdx, ifnull(l.likeCount, 0) likeCount, ifnull(r.replyCount, 0) replyCount\n" +
                        "FROM User u\n" +
                        "INNER JOIN Question q\n" +
                        "on u.userIdx = q.userIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS likeCount FROM `QuestionLike` group by questionIdx) l\n" +
                        "ON q.questionIdx = l.questionIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS replyCount FROM Reply group by questionIdx) r\n" +
                        "ON q.questionIdx = r.questionIdx\n" +
                        "where q.smallCategoryIdx = ?\n and exists(select * from Reply r where(r.questionIdx=q.questionIdx))" +
                        "order by "+ orderBy +" desc\n" +
                        "limit ?, ?";
            } else {
                getQuestionsQuery = "SELECT q.questionIdx, u.userIdx, u.nickname, DATE_FORMAT(q.createdAt, '%m-%d, %y') AS createdAt, q.title, q.bigCategoryIdx, q.smallCategoryIdx, ifnull(l.likeCount, 0) likeCount, ifnull(r.replyCount, 0) replyCount\n" +
                        "FROM User u\n" +
                        "INNER JOIN Question q\n" +
                        "on u.userIdx = q.userIdx\n" +
                        "LEFT JOIN (SELECT questionIdx, count(questionIdx) AS likeCount FROM `QuestionLike` group by questionIdx) l\n" +
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
                        rs.getString("createdAt"),
                        rs.getString("title"),
                        rs.getString("bigCategoryIdx"),
                        rs.getString("smallCategoryIdx"),
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
                "from (select q.questionIdx, nickname, bigCategoryName, smallCategoryName, title, q.createdAt as updatedAt, content, likeCount, replyCount\n" +
                "from Question q, BigCategory b, SmallCategory s , User u, ConceptQuestion c ,\n" +
                "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
                "from Question q left join (select questionIdx, count(*) likeCount from QuestionLike group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
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

    /**
     * hwijeong
     * 코딩 질문 검색 API
     * 입력받은 keyword를 이용해서 질문 받아오기
     * */
    public List<SearchCodingQuestionRes> searchCodingQuestionRes(String keyword) {
        String searchCodingQuestionQuery = "select *\n" +
                "from (select q.questionIdx, nickname, bigCategoryName, smallCategoryName, title, q.createdAt as updatedAt, c.currentError, c.myCodingSkill, likeCount, replyCount\n" +
                "from Question q, BigCategory b, SmallCategory s , User u, CodeQuestion c ,\n" +
                "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
                "from Question q left join (select questionIdx, count(*) likeCount from QuestionLike group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
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

    /**
     * hwijeong
     * 내가 한 질문 조회
     * userIdx를 이용해서 사용자 정보 받은 후
     * 해당 사용자가 한 질문 목록 보여주기
     * */
    public List<MyQuestionListRes> myQuestionListRes (int userIdx) {
        String myQuestionListQuery = "select * \n" +
                "from\n" +
                "(select u.userIdx, q.questionIdx, nickname, bigCategoryName, smallCategoryName, title, q.createdAt as updatedAt, likeCount, replyCount\n" +
                "from Question q, BigCategory b, SmallCategory s , User u, CodeQuestion c ,\n" +
                "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
                "from Question q left join (select questionIdx, count(*) likeCount from QuestionLike group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
                "(select q.questionIdx, ifnull(replyCount,0) as replyCount\n" +
                "from Question q left join (select questionIdx, count(*) replyCount from Reply group by questionIdx) r on q.questionIdx = r.questionIdx) r\n" +
                "where q.bigCategoryIdx = b.bigCategoryIdx and q.smallCategoryIdx = s.smallCategoryIdx and u.userIdx = q.userIdx and c.questionIdx = q.questionIdx \n" +
                "and q.questionIdx = l.questionIdx and q.questionIdx = r.questionIdx\n" +
                "union\n" +
                "select u.userIdx, q.questionIdx,nickname, bigCategoryName, smallCategoryName, title, q.createdAt as updatedAt, likeCount, replyCount\n" +
                "from Question q, BigCategory b, SmallCategory s , User u, ConceptQuestion c ,\n" +
                "(select q.questionIdx, ifnull(likeCount,0) as likeCount\n" +
                "from Question q left join (select questionIdx, count(*) likeCount from QuestionLike group by questionIdx) l on  q.questionIdx = l.questionIdx) l,\n" +
                "(select q.questionIdx, ifnull(replyCount,0) as replyCount\n" +
                "from Question q left join (select questionIdx, count(*) replyCount from Reply group by questionIdx) r on q.questionIdx = r.questionIdx) r\n" +
                "where q.bigCategoryIdx = b.bigCategoryIdx and q.smallCategoryIdx = s.smallCategoryIdx and u.userIdx = q.userIdx and c.questionIdx = q.questionIdx \n" +
                "and q.questionIdx = l.questionIdx and q.questionIdx = r.questionIdx) questionList\n" +
                "where userIdx = ?";
        return this.jdbcTemplate.query(myQuestionListQuery,
                (rs,rowNum) -> new MyQuestionListRes(
                        rs.getLong("userIdx"),
                        rs.getLong("questionIdx"),
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
