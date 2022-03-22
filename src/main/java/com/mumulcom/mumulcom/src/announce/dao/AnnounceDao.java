package com.mumulcom.mumulcom.src.announce.dao;

import com.mumulcom.mumulcom.src.announce.model.AnnounceRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class AnnounceDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<AnnounceRes> getAnnounce() {
        String announceQuery = "select announceTitle, announceContent, DATE_FORMAT(updatedAt, '%m-%d, %y') as updatedAt from Announce";
        return this.jdbcTemplate.query(announceQuery,
                (rs,rowNum) -> new AnnounceRes(
                        rs.getString("announceTitle"),
                        rs.getString("announceContent"),
                        rs.getString("updatedAt")
                ));
    }
}
