package com.mumulcom.mumulcom.src.reply.controller;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import com.mumulcom.mumulcom.src.reply.domain.AdoptRes;
import com.mumulcom.mumulcom.src.reply.domain.MyReplyListRes;
import com.mumulcom.mumulcom.src.reply.domain.ReplyInfoRes;


import com.mumulcom.mumulcom.src.reply.dto.*;


import com.mumulcom.mumulcom.src.reply.provider.ReplyProvider;
import com.mumulcom.mumulcom.src.reply.service.ReplyService;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.mumulcom.mumulcom.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/replies")
public class ReplyController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ReplyService replyService;
    @Autowired
    private final ReplyProvider replyProvider;
    @Autowired
    private final JwtService jwtService;

    public ReplyController(ReplyService replyService, ReplyProvider replyProvider, JwtService jwtService) {
        this.replyService = replyService;
        this.replyProvider = replyProvider;
        this.jwtService = jwtService;
    }

    /**
     * yeji 19번 API
     * reply 생성
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostReplyRes> createReply(@RequestPart(value = "images", required = false) List<MultipartFile> multipartFileList,
                                                  @RequestPart(value = "postReplyReq") PostReplyReq postReplyReq) {
        try {
            if(replyService.checkUserIdx(postReplyReq.getUserIdx()) == 0) {
                return new BaseResponse<>(POST_USERS_INACTIVE_STATUS);
            }

            Long userIdxByJwt = jwtService.getUserIdx();

            if(postReplyReq.getUserIdx() != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<String> imgUrls = replyService.uploadS3image(multipartFileList, postReplyReq.getUserIdx());
            PostReplyRes postReplyRes = replyService.createReply(imgUrls, postReplyReq);

            return new BaseResponse<>(postReplyRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * yeji
     * 전체 답변 조회 API
     */
    @ResponseBody
    @GetMapping("/{questionIdx}/{userIdx}")
    public BaseResponse<List<GetReplyRes>> getReplyList(@PathVariable("questionIdx") int questionIdx, @PathVariable("userIdx") int userIdx) {
        try {
            List<GetReplyRes> getReplyRes = replyService.getReplyList(questionIdx, userIdx);
            return new BaseResponse<>(getReplyRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 휘정
     * 내가 답변한 코딩 질문 조회 API
     * */
    @ResponseBody
    @GetMapping("/my/coding")
    public BaseResponse<List<MyReplyListRes>> myCodingReplyList(@RequestParam long userIdx, @RequestParam(defaultValue = "false") boolean isAdopted) {
        try {
            Long userIdxByJwt = jwtService.getUserIdx();
            if (!userIdxByJwt.equals(userIdx)) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }
            List<MyReplyListRes> myReplyListRes = replyProvider.myCodingReplyListResList(userIdx, isAdopted);
            return new BaseResponse<>(myReplyListRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 휘정
     * 내가 답변한 개념 질문 조회 API
     * */
    @ResponseBody
    @GetMapping("/my/concept")
    public BaseResponse<List<MyReplyListRes>> myConceptReplyList(@RequestParam long userIdx, @RequestParam(defaultValue = "false") boolean isAdopted) {
        try {
            Long userIdxByJwt = jwtService.getUserIdx();
            if (!userIdxByJwt.equals(userIdx)) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }
            List<MyReplyListRes> myReplyListRes = replyProvider.myConceptReplyListResList(userIdx, isAdopted);
            return new BaseResponse<>(myReplyListRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 휘정
     * 채택하기 API
     * Validation : 질문 작성자가 questionIdx로 할 수 있다.
     * 일단 답변 idx에 따른 질문 idx를 가져오고
     * 현재 사용자와 질문 작성자가 같으면 할 수 있게끔
     * */
    @ResponseBody
    @PatchMapping("/adoption/{userIdx}/{replyIdx}")
    public BaseResponse<AdoptRes> adoptReply(@PathVariable("replyIdx") long replyIdx, @PathVariable("userIdx") long userIdx) {
        try {

            // jwt 처리
            Long userIdxByJwt = jwtService.getUserIdx();
            if (!userIdxByJwt.equals(userIdx)) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }

            ReplyInfoRes replyInfo = replyProvider.getReplyInfo(replyIdx);

            // 질문자와 현재 사용자가 같다면 채택 할 수 있게, 안같으면 채택이 불가능하게
            if(userIdx != replyInfo.getWriter()) {
                return new BaseResponse<>(PATCH_ADOPT_NOT_SAME);
            }

            // 그리고 이미 채택된 질문이라면? -> 이제 못하게하기
            if(replyInfo.getStatus().equals("adopted")) {
                return new BaseResponse<>(ALREADY_ADOPTED);
            }

            replyService.adoptReply(replyIdx, replyInfo.getQuestionIdx()); // 채택중
            String result = replyIdx+"번째 답변이 채택되었습니다."; // 몇번째 답변이 채택됨을 알려줌
            String content = "회원님의 답변이 채택되었습니다.";
            AdoptRes adoptRes = replyService.addAdoptionNotice(replyInfo, content);
            return new BaseResponse<>(adoptRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //학준 29. 대답변하기 + 알림넣기
    @ResponseBody
    @PostMapping("/reply")
    public BaseResponse<PostReRepRes> Rereply(@RequestBody PostReReplReq postReReplReq){
        try{
            Long userIdxByJwt = jwtService.getUserIdx();
            if (!userIdxByJwt.equals(postReReplReq.getUserIdx())) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }

            if(postReReplReq.getReplyIdx() == 0 || postReReplReq.getUserIdx() == 0
            || postReReplReq.getContent() == null){
                throw new BaseException(POST_EMPTY_ESSENTIAL_BODY);
            }
            PostReRepRes result = replyService.Rereply(postReReplReq);
            return new BaseResponse<>(result);
        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());

        }
    }

    @ResponseBody
    @GetMapping("/rereply/{replyIdx}")
    public BaseResponse<List<GetReReplyRes>> getReReplies(@PathVariable("replyIdx") long replyIdx){
        try{

            List<GetReReplyRes> getReReplyRes = replyProvider.getReReplies(replyIdx);
            return new BaseResponse<>(getReReplyRes);
        }catch (BaseException baseException){
            return new BaseResponse<>(baseException.getStatus());
        }
    }



}
