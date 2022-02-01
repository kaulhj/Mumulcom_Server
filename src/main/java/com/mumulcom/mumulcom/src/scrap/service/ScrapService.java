package com.mumulcom.mumulcom.src.scrap.service;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import com.mumulcom.mumulcom.src.scrap.dto.PostScrapReq;
import com.mumulcom.mumulcom.src.scrap.dao.ScrapDao;
import com.mumulcom.mumulcom.src.scrap.provider.ScrapProvider;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class ScrapService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ScrapDao scrapDao;
    private final ScrapProvider scrapProvider;
    private final JwtService jwtService;

    @Autowired
    public ScrapService(ScrapDao scrapDao, ScrapProvider scrapProvider, JwtService jwtService){
        this.scrapDao = scrapDao;
        this.scrapProvider = scrapProvider;
        this.jwtService = jwtService;
    }


    //24.
    public String createScrap(PostScrapReq postScrapReq) throws BaseException{
        if(!scrapProvider.scrapAuth(postScrapReq))
            throw new BaseException(BaseResponseStatus.POST_INVALID_SCRAP_AUTH);
        try{
            String result = scrapDao.createScrap(postScrapReq);
            return new String(result);
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

}
