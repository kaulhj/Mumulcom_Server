package com.mumulcom.mumulcom.src.reply.service;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.src.reply.dao.ReplyDao;

import com.mumulcom.mumulcom.src.reply.domain.ReplyInfoRes;


import com.mumulcom.mumulcom.src.reply.dto.GetReplyRes;
import com.mumulcom.mumulcom.src.reply.dto.PostReReplReq;


import com.mumulcom.mumulcom.src.reply.dto.PostReplyReq;
import com.mumulcom.mumulcom.src.reply.dto.PostReplyRes;
import com.mumulcom.mumulcom.src.reply.provider.ReplyProvider;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


import java.util.List;

import static com.mumulcom.mumulcom.config.BaseResponseStatus.*;


@Service
@Transactional
public class ReplyService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReplyDao replyDao;
    private final JwtService jwtService;
    private final ReplyProvider replyProvider;

    public ReplyService(ReplyDao replyDao, JwtService jwtService, ReplyProvider replyProvider) {
        this.replyDao = replyDao;
        this.jwtService = jwtService;
        this.replyProvider = replyProvider;
    }

    /**
     * yeji
     * 답변 생성 API
     */
    public PostReplyRes createReply(PostReplyReq postReplyReq) throws BaseException {
        try {
            PostReplyRes postReplyRes = replyDao.creatReply(postReplyReq);
            return postReplyRes;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * yeji
     * 전체 답변 조회 API
     */
    public List<GetReplyRes> getReplyList(int questionIdx) throws BaseException {
        try {
            List<GetReplyRes> getReplyRes = replyDao.getReplyList(questionIdx);
            return getReplyRes;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 휘정 채택 API
     * */
    @Transactional
    public void adoptReply(long replyIdx) throws BaseException {
        try {
            int result = replyDao.adoptReply(replyIdx);
            if(result == 0) {
                throw new BaseException(FAILED_ADOPT_REPLY);
            }
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }


    @Transactional
    public void addAdoptionNotice(ReplyInfoRes replyInfoRes, String content) throws BaseException {
        try {
            replyDao.addAdoptionNotice(replyInfoRes,content);
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
          exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //29
    public String Rereply(PostReReplReq postReReplReq) throws BaseException{

        if(replyProvider.reReplyAuth(postReReplReq) == 0)
                throw new BaseException(POST_INVALID_REREPLY_AUTH);
        try{
            String result = replyDao.rereply(postReReplReq);
            return result;
        }catch (Exception exception){
          exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
