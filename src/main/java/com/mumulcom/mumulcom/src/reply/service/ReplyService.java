package com.mumulcom.mumulcom.src.reply.service;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.src.reply.dao.ReplyDao;
import com.mumulcom.mumulcom.src.reply.dto.PostReplyReq;
import com.mumulcom.mumulcom.src.reply.dto.PostReplyRes;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.mumulcom.mumulcom.config.BaseResponseStatus.DATABASE_ERROR;
import static com.mumulcom.mumulcom.config.BaseResponseStatus.FAILED_ADOPT_REPLY;

@Service
@Transactional
public class ReplyService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReplyDao replyDao;
    private final JwtService jwtService;

    public ReplyService(ReplyDao replyDao, JwtService jwtService) {
        this.replyDao = replyDao;
        this.jwtService = jwtService;
    }

    /**
     * yeji 8번 API
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
     * 휘정 채택 API
     * */
    @Transactional
    public void adoptReply(int replyIdx) throws BaseException {
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
}
