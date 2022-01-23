package com.mumulcom.mumulcom.src.like.dao;


import com.mumulcom.mumulcom.src.like.dto.PostLikeReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository

public class LikeDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String createLike(PostLikeReq postLikeReq){

            String createLikeQuery = "INSERT INTO `Like`(QUESTIONIDX, USERIDX) VALUES (?, ?)";
            Object[] createLikeParams = new Object[]{postLikeReq.getQuestionIdx(),
            postLikeReq.getUserIdx()};
            this.jdbcTemplate.update(createLikeQuery,createLikeParams);

            return new String("해당글을 좋아요 하였습니다");

    }
}
