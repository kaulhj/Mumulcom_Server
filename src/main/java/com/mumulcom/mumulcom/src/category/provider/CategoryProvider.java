package com.mumulcom.mumulcom.src.category.provider;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.src.category.dao.CategoryDao;
import com.mumulcom.mumulcom.src.category.dto.GetCategoryRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.mumulcom.mumulcom.config.BaseResponseStatus.DATABASE_ERROR;
import static com.mumulcom.mumulcom.config.BaseResponseStatus.POST_USERS_INVALID_CATEGORY;

@Service
public class CategoryProvider {
    private final CategoryDao categoryDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CategoryProvider(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public List<GetCategoryRes> getCategoryResList() throws BaseException {
        try {
            List<GetCategoryRes> getCategoryResList = categoryDao.getCategoryList();
            return getCategoryResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public Optional<Long> getSmallCategoryIdxByName(String smallCategoryName) throws BaseException {
        try {
            return categoryDao.getSmallCategoryIdxByName(smallCategoryName);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new BaseException(POST_USERS_INVALID_CATEGORY);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public Optional<String> getSmallCategoryNameByIdx(Long smallCategoryIdx) throws BaseException {
        try {
            return categoryDao.getSmallCategoryNameByIdx(smallCategoryIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
