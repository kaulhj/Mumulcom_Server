package com.mumulcom.mumulcom.src.category.dao;

import com.mumulcom.mumulcom.src.category.domain.GetCategoryRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class GetCategoryListDao {

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetCategoryRes> getCategoryList() {
        String getCategoryListQuery = "select b.bigCategoryIdx, b.bigCategoryName, s.smallCategoryIdx, s.smallCategoryName\n" +
                "from BigCategory b join SmallCategory s on b.bigCategoryIdx = s.bigCategoryIdx";
        return this.jdbcTemplate.query(getCategoryListQuery,
                (rs, rowNum) -> new GetCategoryRes(
                        rs.getLong("bigCategoryIdx"),
                        rs.getString("bigCategoryName"),
                        rs.getLong("smallCategoryIdx"),
                        rs.getString("smallCategoryName")
                ));
    }

}
