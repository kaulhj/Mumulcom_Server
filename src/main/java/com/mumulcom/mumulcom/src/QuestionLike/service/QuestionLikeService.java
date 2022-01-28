package com.mumulcom.mumulcom.src.QuestionLike.service;


import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import com.mumulcom.mumulcom.src.QuestionLike.dao.QuestionLikeDao;
import com.mumulcom.mumulcom.src.QuestionLike.dto.PostLikeReq;
import com.mumulcom.mumulcom.src.QuestionLike.provider.QuestionLikeProvider;
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


    public String createLike(PostLikeReq postLikeReq)throws BaseException{
        try{
            String result = questionLikeDao.createLike(postLikeReq);
            return new String(result);
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }



}
