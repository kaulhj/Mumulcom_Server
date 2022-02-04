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
import com.mumulcom.mumulcom.src.question.provider.*;

@Service

public class ScrapService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ScrapDao scrapDao;
    private final ScrapProvider scrapProvider;
    private final JwtService jwtService;
    private final QuestionProvider questionProvider;

    @Autowired
    public ScrapService(ScrapDao scrapDao, ScrapProvider scrapProvider, JwtService jwtService, QuestionProvider questionProvider){
        this.scrapDao = scrapDao;
        this.scrapProvider = scrapProvider;
        this.jwtService = jwtService;
        this.questionProvider = questionProvider;
    }


    //학준 24.
    public String createScrap(PostScrapReq postScrapReq) throws BaseException{

        try{
            String result = null;
            if(scrapProvider.queIdxExist(postScrapReq.getQuestionIdx()) == 0)
                throw new BaseException(BaseResponseStatus.GET_QUESTIONS_EMPTY_DATA);
            if(scrapProvider.scrapAuth(postScrapReq) == 0)
                throw new BaseException(BaseResponseStatus.POST_INVALID_SCRAP_AUTH);
            if(questionProvider.checkUserStatus(postScrapReq.getUserIdx()) == 0)
                throw new BaseException(BaseResponseStatus.POST_USERS_INACTIVE_STATUS);
            switch (scrapProvider.getScrapStatues(postScrapReq)){
                case 1:
                    result = scrapDao.createScrap(postScrapReq, 1);
                    break;
                case 2:
                    result = scrapDao.createScrap(postScrapReq, 2);
                    break;
                case 3:
                    result = scrapDao.createScrap(postScrapReq, 3);

            }
            return new String(result);
        }catch (BaseException baseException){
            baseException.printStackTrace();
            throw new BaseException(baseException.getStatus());
        }   catch(Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

}
