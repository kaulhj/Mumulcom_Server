package com.mumulcom.mumulcom.src.question.provider;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.src.question.dao.MyQuestionListDao;
import com.mumulcom.mumulcom.src.question.domain.MyQuestionListRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mumulcom.mumulcom.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class MyQuestionProvider {
    private MyQuestionListDao myQuestionListDao;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public MyQuestionProvider(MyQuestionListDao myQuestionListDao) {
        this.myQuestionListDao = myQuestionListDao;
    }

    public List<MyQuestionListRes> myQuestionListResList (int userIdx) throws BaseException {
        try {
            List<MyQuestionListRes> myQuestionListResList = myQuestionListDao.myQuestionListRes(userIdx);
            return myQuestionListResList;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
