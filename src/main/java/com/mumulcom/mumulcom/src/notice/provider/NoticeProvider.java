package com.mumulcom.mumulcom.src.notice.provider;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.src.notice.dao.NoticeDao;
import com.mumulcom.mumulcom.src.notice.repository.GetNoticeRes;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mumulcom.mumulcom.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class NoticeProvider {
    private final NoticeDao noticeDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public NoticeProvider( NoticeDao noticeDao, JwtService jwtService){
        this.noticeDao = noticeDao;
        this.jwtService = jwtService;
    }

    public List<GetNoticeRes> getNoticeResList(long userIcx) throws BaseException {
        try {
            List<GetNoticeRes> getNoticeList = noticeDao.noticeList(userIcx);
            return getNoticeList;
        } catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
