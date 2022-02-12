package com.mumulcom.mumulcom.src.like.service;


import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import com.mumulcom.mumulcom.src.like.dao.QuestionLikeDao;
import com.mumulcom.mumulcom.src.like.dto.PostLikeRes;
import com.mumulcom.mumulcom.src.like.dto.PostQueLikeReq;

import com.mumulcom.mumulcom.src.like.dto.PostReplyLikeReq;
import com.mumulcom.mumulcom.src.like.provider.QuestionLikeProvider;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mumulcom.mumulcom.src.question.provider.*;

@Service
public class QuestionLikeService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final QuestionLikeDao questionLikeDao;
    private final QuestionLikeProvider questionLikeProvider;
    private final JwtService jwtService;
    private final QuestionProvider questionProvider;

    @Autowired
    public QuestionLikeService(QuestionLikeDao questionLikeDao, QuestionLikeProvider questionLikeProvider, JwtService jwtService
    , QuestionProvider questionProvider){
        this.questionLikeDao = questionLikeDao;
        this.questionLikeProvider = questionLikeProvider;
        this.jwtService = jwtService;
        this.questionProvider = questionProvider;
    }

//학준 25
    public PostLikeRes createQuestionLike(PostQueLikeReq postQueLikeReq)throws BaseException{
        try{
            PostLikeRes result = null;
            if(questionLikeProvider.likeQueIdxExist(postQueLikeReq.getQuestionIdx()) == 0)
                throw new BaseException(BaseResponseStatus.GET_QUESTIONS_EMPTY_DATA);
            if(questionLikeProvider.checkUserStatus(postQueLikeReq) != 0)
                throw new BaseException(BaseResponseStatus.POST_USERS_INACTIVE_STATUS);
            if(questionLikeDao.getTargetUserIdx(postQueLikeReq.getQuestionIdx()) == 0)
                throw new BaseException(BaseResponseStatus.GET_TARGETUSERS_EMPTY_DATA);
            Long targetUserIdx = questionLikeDao.getTargetUserIdx(postQueLikeReq.getQuestionIdx());
            switch (questionLikeProvider.getLikeStatus(postQueLikeReq)){
                case 1:     //생성된 게 없을 때
                    result = questionLikeDao.createQuestionLike(postQueLikeReq, 1);
                    break;
                case 2: // 생성됐으나 inactive상태
                    result = questionLikeDao.createQuestionLike(postQueLikeReq, 2);
                    break;
                case 3: //생성됐으나 active상태
                    result = questionLikeDao.createQuestionLike(postQueLikeReq, 3);

            }
            return result;
        }catch(BaseException baseException) {
            baseException.printStackTrace();
            throw new BaseException(baseException.getStatus());
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }


    //26
    public PostLikeRes createReplyLike(PostReplyLikeReq postReplyLikeReq)throws BaseException{
        try{
            PostLikeRes result = null;
            if(questionLikeProvider.likeReplyIdxExist(postReplyLikeReq.getReplyIdx()) == 0)
                throw new BaseException(BaseResponseStatus.GET_QUESTIONS_EMPTY_DATA);
            if(questionLikeProvider.checkUserStatus(postReplyLikeReq) != 0)
                throw new BaseException(BaseResponseStatus.POST_USERS_INACTIVE_STATUS);
            if(questionLikeDao.replyNotTarInd(postReplyLikeReq.getReplyIdx()) == 0)
                throw new BaseException(BaseResponseStatus.GET_TARGETUSERS_EMPTY_DATA);
            switch (questionLikeProvider.getLikeStatus(postReplyLikeReq)){
                case 1:     //생성된 게 없을 때
                    result = questionLikeDao.createReplyLike(postReplyLikeReq, 1);
                    break;
                case 2: // 생성됐으나 inactive상태
                    result = questionLikeDao.createReplyLike(postReplyLikeReq, 2);
                    break;
                case 3: //생성됐으나 active상태
                    result = questionLikeDao.createReplyLike(postReplyLikeReq, 3);

            }
            return result;
        }catch(BaseException baseException) {
            baseException.printStackTrace();
            throw new BaseException(baseException.getStatus());
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }



}