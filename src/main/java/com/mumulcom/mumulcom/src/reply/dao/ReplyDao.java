package com.mumulcom.mumulcom.src.reply.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ReplyDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ReplyDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * yeji 8번 API
     * 답변 생성
     */

}
