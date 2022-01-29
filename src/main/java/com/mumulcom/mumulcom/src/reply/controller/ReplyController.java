package com.mumulcom.mumulcom.src.reply.controller;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.src.reply.domain.MyReplyListRes;
import com.mumulcom.mumulcom.src.reply.dto.PostReplyReq;
import com.mumulcom.mumulcom.src.reply.dto.PostReplyRes;
import com.mumulcom.mumulcom.src.reply.provider.ReplyProvider;
import com.mumulcom.mumulcom.src.reply.service.ReplyService;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mumulcom.mumulcom.config.BaseResponseStatus.PATCH_ADOPT_NOT_SAME;

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
     * yeji 8번 API
     * reply 생성
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostReplyRes> createReply(@RequestBody PostReplyReq postReplyReq) {
        try {
            PostReplyRes postReplyRes = replyService.createReply(postReplyReq);
            return new BaseResponse<>(postReplyRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 휘정
     * 내가 답변한 질문 조회 API
     * */
    @ResponseBody
    @GetMapping("/my/{userIdx}")
    public BaseResponse<List<MyReplyListRes>> myReplyList(@PathVariable("userIdx") int userIdx) {
        try {
            List<MyReplyListRes> myReplyListRes = replyProvider.myReplyListResList(userIdx);
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
    @Transactional
    @PatchMapping("/adoption/{userIdx}/{replyIdx}")
    public BaseResponse<String> adoptReply(@PathVariable("replyIdx") int replyIdx, @PathVariable("userIdx") int userIdx) {
        try {
            int questionWriterIdx = replyProvider.getQuestionWriter(replyIdx);

            if(userIdx != questionWriterIdx) {
                return new BaseResponse<>(PATCH_ADOPT_NOT_SAME);
            }

            replyService.adoptReply(replyIdx);
            String result = replyIdx+"번째 답변이 채택되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
