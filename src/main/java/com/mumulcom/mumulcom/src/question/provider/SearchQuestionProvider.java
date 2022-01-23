package com.mumulcom.mumulcom.src.question.provider;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.src.question.dao.SearchQuestionDao;
import com.mumulcom.mumulcom.src.question.domain.SearchCodingQuestionRes;
import com.mumulcom.mumulcom.src.question.domain.SearchConceptQuestionRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mumulcom.mumulcom.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class SearchQuestionProvider {
    private final SearchQuestionDao searchQuestionDao;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SearchQuestionProvider(SearchQuestionDao searchQuestionDao) {
        this.searchQuestionDao = searchQuestionDao;
    }

    public List<SearchConceptQuestionRes> searchConceptQuestionResList(String keyword) throws BaseException {
        try {
            List<SearchConceptQuestionRes> searchConceptQuestionResList = searchQuestionDao.searchConceptQuestionRes(keyword);
            return searchConceptQuestionResList;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<SearchCodingQuestionRes> searchCodingQuestionResList(String keyword) throws BaseException {
        try {
            List<SearchCodingQuestionRes> searchCodingQuestionResList = searchQuestionDao.searchCodingQuestionRes(keyword);
            return searchCodingQuestionResList;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
