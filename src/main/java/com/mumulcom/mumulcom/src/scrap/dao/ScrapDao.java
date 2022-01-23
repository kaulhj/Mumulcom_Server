package com.mumulcom.mumulcom.src.scrap.dao;


import com.mumulcom.mumulcom.src.scrap.dto.PostScrapReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository

public class ScrapDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String createScrap(PostScrapReq postScrapReq){

        String createScrapQuery = "INSERT INTO Scrap(QUESTIONIDX, USERIDX) VALUES (?, ?)";
        Object[] createScrapParams = new Object[]{postScrapReq.getQuestionIdx(),
        postScrapReq.getUserIdx()};

        this.jdbcTemplate.update(createScrapQuery,createScrapParams);
        return new String("해당 글을 스크랩 하였습니다");
    }


}
