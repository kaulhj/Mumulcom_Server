package com.mumulcom.mumulcom.src.category.provider;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.src.category.dao.GetCategoryListDao;
import com.mumulcom.mumulcom.src.category.domain.GetCategoryRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mumulcom.mumulcom.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class GetCategoryListProvider {
    private final GetCategoryListDao getCategoryListDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public GetCategoryListProvider(GetCategoryListDao getCategoryListDao) {
        this.getCategoryListDao = getCategoryListDao;
    }

    public List<GetCategoryRes> getCategoryResList() throws BaseException {
        try {
            List<GetCategoryRes> getCategoryResList = getCategoryListDao.getCategoryList();
            return getCategoryResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
