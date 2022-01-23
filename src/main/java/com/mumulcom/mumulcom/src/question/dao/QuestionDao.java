package com.mumulcom.mumulcom.src.question.dao;


import com.mumulcom.mumulcom.src.question.dto.CodeQuestionReq;
import com.mumulcom.mumulcom.src.question.dto.ConceptQueReq;
import com.mumulcom.mumulcom.src.question.dto.GetRecQueRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository

public class QuestionDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //2.
    public GetRecQueRes getRecentQuestion(long userIdx){
        //like값 가져오기
        String getRecQueQuery1 = "SELECT\n" +
                "       count(CASE WHEN Q.questionIdx = L.questionIdx then 1 END )as LikeCount\n" +
                "FROM\n" +
                "Question Q\n" +
                "INNER JOIN `Like` L ON Q.questionIdx = L.questionIdx\n" +
                "where Q.userIdx = ?\n" +
                "group by Q.questionIdx\n" +
                "order by Q.questionIdx desc limit 1";
        long getRecQueParam = userIdx;
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


    @Transactional
    public String codeQuestion(CodeQuestionReq codeQuestionReq) {




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
        String InsImgTabQuery = "INSERT INTO Image(questionIdx, imageUrl) VALUES (?, ?)";
        for (int i = 0; i < codeQuestionReq.getImageUrls().size(); i++) {
            Object[] InsImgTabParams = new Object[]{lastQueId, codeQuestionReq.getImageUrls().get(i)};
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

        String CodQueTabQue = "INSERT INTO ConceptQuestion(questionIdx, currentError,myCodingSkill)\n" +
                "VALUES (?,?,?)";
        Object[] CodQueTabParams = new Object[]{lastQueId, conceptQueReq.getCurrentError(),
                conceptQueReq.getMyCodingSkill()};

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

}
