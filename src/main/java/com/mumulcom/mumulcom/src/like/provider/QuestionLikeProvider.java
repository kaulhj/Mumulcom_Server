package com.mumulcom.mumulcom.src.like.provider;


import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import com.mumulcom.mumulcom.src.like.dao.QuestionLikeDao;
import com.mumulcom.mumulcom.src.like.dto.PostQueLikeReq;
import com.mumulcom.mumulcom.src.like.dto.PostReplyLikeReq;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mumulcom.mumulcom.config.BaseResponseStatus.DATABASE_ERROR;

@Service

public class QuestionLikeProvider {
    private final QuestionLikeDao questionLikeDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired

    public QuestionLikeProvider(QuestionLikeDao questionLikeDao, JwtService jwtService) {
        this.questionLikeDao = questionLikeDao;
        this.jwtService = jwtService;
    }

    //25.1
    public boolean likeAuth(PostQueLikeReq postQueLikeReq) throws BaseException {
        try {
            boolean LikeAuth = questionLikeDao.LikeAuth(postQueLikeReq);
            return LikeAuth;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }


    //25.2
    public boolean likeQueIdxExist(Long getQuestionIdx) throws BaseException {
        try {
            boolean queId = questionLikeDao.likeQueIdExist(getQuestionIdx);
            return queId;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }

    }

    //26.1

    public boolean likeAuth(PostReplyLikeReq postReplyLikeReq) throws BaseException {
        try {
            boolean LikeAuth = questionLikeDao.LikeAuth(postReplyLikeReq);
            return LikeAuth;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }


}
