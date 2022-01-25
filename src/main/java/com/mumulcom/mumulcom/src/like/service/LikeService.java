package com.mumulcom.mumulcom.src.like.service;


import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import com.mumulcom.mumulcom.src.like.dao.LikeDao;
import com.mumulcom.mumulcom.src.like.dto.PostLikeReq;
import com.mumulcom.mumulcom.src.like.provider.LikeProvider;
import com.mumulcom.mumulcom.src.scrap.provider.ScrapProvider;
import com.mumulcom.mumulcom.src.scrap.service.ScrapService;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final LikeDao likeDao;
    private final LikeProvider likeProvider;
    private final JwtService jwtService;

    @Autowired
    public LikeService(LikeDao likeDao, LikeProvider likeProvider, JwtService jwtService){
        this.likeDao = likeDao;
        this.likeProvider = likeProvider;
        this.jwtService = jwtService;
    }


    public String createLike(PostLikeReq postLikeReq)throws BaseException{
        try{
            String result = likeDao.createLike(postLikeReq);
            return new String(result);
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }



}
