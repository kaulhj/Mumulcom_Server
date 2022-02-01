package com.mumulcom.mumulcom.src.reply.provider;


import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.src.reply.dao.ReplyDao;
import com.mumulcom.mumulcom.src.reply.domain.MyReplyListRes;
import com.mumulcom.mumulcom.src.reply.domain.ReplyInfoRes;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mumulcom.mumulcom.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ReplyProvider {
    private final ReplyDao replyDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ReplyProvider(ReplyDao replyDao, JwtService jwtService){
        this.replyDao = replyDao;
        this.jwtService = jwtService;
    }

    public List<MyReplyListRes> myReplyListResList (int userIdx) throws BaseException {
        try {
            List<MyReplyListRes> myReplyListRes = replyDao.myReplyListResList(userIdx);
            return myReplyListRes;
        } catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public ReplyInfoRes getReplyInfo (int replyIdx) throws BaseException {
        try {
            ReplyInfoRes replyInfo = replyDao.getReplyInfo(replyIdx);
            return replyInfo;
        }  catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
