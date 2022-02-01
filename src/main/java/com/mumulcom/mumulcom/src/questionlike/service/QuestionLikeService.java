package com.mumulcom.mumulcom.src.questionlike.service;


import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import com.mumulcom.mumulcom.src.questionlike.dao.QuestionLikeDao;
import com.mumulcom.mumulcom.src.questionlike.dto.PostQueLikeReq;
import com.mumulcom.mumulcom.src.questionlike.dto.PostReplyLikeReq;
import com.mumulcom.mumulcom.src.questionlike.provider.QuestionLikeProvider;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionLikeService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final QuestionLikeDao questionLikeDao;
    private final QuestionLikeProvider questionLikeProvider;
    private final JwtService jwtService;

    @Autowired
    public QuestionLikeService(QuestionLikeDao questionLikeDao, QuestionLikeProvider questionLikeProvider, JwtService jwtService){
        this.questionLikeDao = questionLikeDao;
        this.questionLikeProvider = questionLikeProvider;
        this.jwtService = jwtService;
    }


    public String createQuestionLike(PostQueLikeReq postQueLikeReq)throws BaseException{
        try{
            if(!questionLikeProvider.likeAuth(postQueLikeReq))
                throw new BaseException(BaseResponseStatus.POST_INVALID_LIKE_AUTH);
            if(!questionLikeProvider.likeQueIdxExist(postQueLikeReq.getQuestionIdx()))
                throw new BaseException(BaseResponseStatus.POST_QUESTIONS_INVALID_CATEGORY_RANGE);
            String result = questionLikeDao.createLike(postQueLikeReq);
            return new String(result);
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public String createReplyLike(PostReplyLikeReq postReplyLikeReq)throws BaseException{
        try{
            if(!questionLikeProvider.likeAuth(postReplyLikeReq))
                throw new BaseException(BaseResponseStatus.POST_INVALID_LIKE_AUTH);
            if(!questionLikeProvider.likeQueIdxExist(postReplyLikeReq.getReplyIdx()))
                throw new BaseException(BaseResponseStatus.POST_QUESTIONS_INVALID_CATEGORY_RANGE);
            String result = questionLikeDao.createReplyLike(postReplyLikeReq);
            return new String(result);
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }



}
