package com.mumulcom.mumulcom.src.scrap.provider;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import com.mumulcom.mumulcom.src.scrap.dao.ScrapDao;
import com.mumulcom.mumulcom.src.scrap.domain.MyScrapListRes;
import com.mumulcom.mumulcom.src.scrap.dto.PostScrapReq;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.mumulcom.mumulcom.config.BaseResponseStatus.DATABASE_ERROR;

import java.util.List;

@Service

public class ScrapProvider {

    private final ScrapDao scrapDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ScrapProvider(ScrapDao scrapDao, JwtService jwtService){
        this.scrapDao = scrapDao;
        this.jwtService = jwtService;
    }

    public List<MyScrapListRes> myScrapListRes (int userIdx) throws BaseException {
        try {
            List<MyScrapListRes> myScrapListRes = scrapDao.myScrapListRes(userIdx);
            return myScrapListRes;
        } catch (Exception exception){{
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }}
    }

    public List<MyScrapListRes> myScrapListRes (int userIdx, String bigCategoryName) throws BaseException {
        try {
            List<MyScrapListRes> myScrapListRes = scrapDao.myScrapListRes(userIdx,bigCategoryName);
            return myScrapListRes;
        } catch (Exception exception){{
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }}
    }

    public List<MyScrapListRes> myScrapListRes (int userIdx, String bigCategoryName, String smallCategoryName) throws BaseException {
        try {
            List<MyScrapListRes> myScrapListRes = scrapDao.myScrapListRes(userIdx,bigCategoryName,smallCategoryName);
            return myScrapListRes;
        } catch (Exception exception){{
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }}
    }

    //24. 자신의 스크랩 질문인지 확인
    public boolean scrapAuth(PostScrapReq postScrapReq)throws BaseException{
        try{
            boolean scrapAuth = scrapDao.scrapAuth(postScrapReq);
            return scrapAuth;
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
    public int queIdxExist(Long getQuestionIdx)throws BaseException{
        try{
            int queId = scrapDao.queIdExist(getQuestionIdx);
            return queId;
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
