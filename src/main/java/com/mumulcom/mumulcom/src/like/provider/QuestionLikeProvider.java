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

    //25.2
    public int likeAuth(PostQueLikeReq postQueLikeReq) throws BaseException {
        try {
            int LikeAuth = questionLikeDao.LikeAuth(postQueLikeReq);
            return LikeAuth;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }


    //25.1
    public int likeQueIdxExist(Long getQuestionIdx) throws BaseException {
        try {
            int queId = questionLikeDao.likeQueIdExist(getQuestionIdx);
            return queId;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }

    }

    //학준26.2
    public int likeReplyIdxExist(Long getReplyIdx) throws BaseException {
        try {
            int queId = questionLikeDao.likeReplyIdExist(getReplyIdx);
            return queId;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //학준 25.3
    public int checkUserStatus(PostQueLikeReq postQueLikeReq) throws BaseException{
        try {
            int validNum = questionLikeDao.checkUserStatus(postQueLikeReq);  //active면 0, inactive면 1
            return validNum;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //26.3
    public int checkUserStatus(PostReplyLikeReq postQueLikeReq) throws BaseException{
        try {
            int validNum = questionLikeDao.checkUserStatus(postQueLikeReq);  //active면 0, inactive면 1
            return validNum;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //25.4
    public int getLikeStatus(PostQueLikeReq postQueLikeReq)throws BaseException{
        try{
            int LikeStatusNum = questionLikeDao.getLikeStatus(postQueLikeReq);
            return LikeStatusNum;
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //26.4
    public int getLikeStatus(PostReplyLikeReq postReplyLikeReq)throws BaseException{
        try{
            int LikeStatusNum = questionLikeDao.getLikeStatus(postReplyLikeReq);
            return LikeStatusNum;
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //26.1

    public int ReplylikeAuth(PostReplyLikeReq postReplyLikeReq) throws BaseException {
        try {
            int LikeAuth = questionLikeDao.ReplyLikeAuth(postReplyLikeReq);
            return LikeAuth;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }





}