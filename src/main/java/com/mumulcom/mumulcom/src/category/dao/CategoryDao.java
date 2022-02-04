package com.mumulcom.mumulcom.src.category.dao;

import com.mumulcom.mumulcom.src.category.dto.GetCategoryRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoryDao {

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

    public Optional<Long> getSmallCategoryIdxByName(String smallCategoryName) {
        String getSmallCategoryIdxByNameQuery = "select s.smallCategoryIdx\n" +
                "from SmallCategory s where s.smallCategoryName = ?";
        Long smallCategoryIdx = this.jdbcTemplate.queryForObject(getSmallCategoryIdxByNameQuery,
                (rs, rowNum) -> rs.getLong("smallCategoryIdx"),
                smallCategoryName
        );
        return Optional.ofNullable(smallCategoryIdx);
    }

    public Optional<String> getSmallCategoryNameByIdx(Long smallCategoryIdx) {
        String getSmallCategoryIdxByNameQuery = "select s.smallCategoryName\n" +
                "from SmallCategory s where s.smallCategoryIdx = ?";
        String smallCategoryName = this.jdbcTemplate.queryForObject(getSmallCategoryIdxByNameQuery,
                (rs, rowNum) -> rs.getString("smallCategoryName"),
                smallCategoryIdx
        );
        return Optional.ofNullable(smallCategoryName);
    }
}
