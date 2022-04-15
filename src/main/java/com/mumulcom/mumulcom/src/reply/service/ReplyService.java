package com.mumulcom.mumulcom.src.reply.service;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import com.mumulcom.mumulcom.src.reply.dao.ReplyDao;

import com.mumulcom.mumulcom.src.reply.domain.AdoptRes;
import com.mumulcom.mumulcom.src.reply.domain.ReplyInfoRes;


import com.mumulcom.mumulcom.src.reply.dto.*;


import com.mumulcom.mumulcom.src.reply.provider.ReplyProvider;
import com.mumulcom.mumulcom.src.s3.service.S3Uploader;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mumulcom.mumulcom.config.BaseResponseStatus.*;


@Service
@Transactional
public class ReplyService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReplyDao replyDao;
    private final JwtService jwtService;
    private final ReplyProvider replyProvider;
    private final S3Uploader s3Uploader;

    public ReplyService(ReplyDao replyDao, JwtService jwtService, ReplyProvider replyProvider, S3Uploader s3Uploader) {
        this.replyDao = replyDao;
        this.jwtService = jwtService;
        this.replyProvider = replyProvider;
        this.s3Uploader = s3Uploader;
    }

    /**
     * yeji
     * 답변 생성 API
     */
    public PostReplyRes createReply(List<String> imgUrls, PostReplyReq postReplyReq) throws BaseException {
        try {
            PostReplyRes postReplyRes = replyDao.creatReply(imgUrls, postReplyReq);
            return postReplyRes;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * yeji
     * 유저 존재 여부 확인
     */
    public int checkUserIdx(Long userIdx) throws BaseException {
        try {
            return replyDao.checkUserIdx(userIdx);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 답변 생성 s3 업로드
     */
    public List<String> uploadS3image(List<MultipartFile> multipartFileList, Long userIdx) {
        try {

            List<String> imagePath1 = s3Uploader.upload(multipartFileList, "userIdx"+ String.valueOf(userIdx));
            return imagePath1;
        }catch (NullPointerException nullPointerException) {
            return new ArrayList<>();
        }catch(Exception exception){
            exception.printStackTrace();
            List<String> mylist = Collections.singletonList("이미지 전송 실패");
            return mylist;
        }
    }

    /**
     * yeji
     * 전체 답변 조회 API
     */
    public List<GetReplyRes> getReplyList(Long questionIdx, Long userIdx) throws BaseException {
        try {
            List<GetReplyRes> getReplyRes = replyDao.getReplyList(questionIdx, userIdx);
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
    public void adoptReply(long replyIdx, long questionIdx) throws BaseException {
        try {
            int result = replyDao.adoptReply(replyIdx);
            replyDao.updateStatus(questionIdx);
            if(result == 0) {
                throw new BaseException(FAILED_ADOPT_REPLY);
            }
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }


    @Transactional
    public AdoptRes addAdoptionNotice(ReplyInfoRes replyInfoRes, String content) throws BaseException {
        try {
            replyDao.addAdoptionNotice(replyInfoRes,content);
            AdoptRes adoptRes = new AdoptRes(replyInfoRes.getAnswerer(),replyInfoRes.getQuestionIdx(), content);
            return adoptRes;
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //29
    public PostReRepRes Rereply(String imgUrls, PostReReplReq postReReplReq) throws BaseException{


        try{
            if(replyProvider.repIdxExist(postReReplReq.getReplyIdx()) == 0)
                throw new BaseException(BaseResponseStatus.GET_REPLIES_EMPTY_DATA);
            if(replyProvider.reReplyAuth(postReReplReq) == 0)
                throw new BaseException(POST_INVALID_REREPLY_AUTH);
            Long targetUserIdx = replyDao.getTargetUserIndex(postReReplReq.getReplyIdx());
            if(targetUserIdx == null)
                throw new BaseException(GET_TARGETUSERS_EMPTY_DATA);
            PostReRepRes result = replyDao.rereply(imgUrls, postReReplReq, targetUserIdx);
            return result;
        }catch(BaseException baseException){
            baseException.printStackTrace();
            throw new BaseException(baseException.getStatus());
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }


    //18. 이미지 1개 s3에 저장
    public String uploadS3image1(MultipartFile multipartFile, Long userIdx)throws BaseException{
        try{
            String imagePath1 = s3Uploader.upload1(multipartFile, "userIdx" + String.valueOf(userIdx));
            return imagePath1;
        }catch(Exception exception) {
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.POST_IMAGES_FAILED);
        }
    }
}