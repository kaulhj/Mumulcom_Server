package com.mumulcom.mumulcom.src.questionlike.controller;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.src.questionlike.provider.QuestionLikeProvider;
import com.mumulcom.mumulcom.src.questionlike.dto.PostQueLikeReq;
import com.mumulcom.mumulcom.src.questionlike.dto.PostReplyLikeReq;
import com.mumulcom.mumulcom.src.questionlike.service.QuestionLikeService;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/likes")

public class QuestionLikeController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final QuestionLikeProvider questionLikeProvider;
    @Autowired
    private final QuestionLikeService questionLikeService;
    @Autowired
    private final JwtService jwtService;

    public QuestionLikeController(QuestionLikeProvider questionLikeProvider, QuestionLikeService questionLikeService, JwtService jwtService){
        this.questionLikeProvider = questionLikeProvider;
        this.questionLikeService = questionLikeService;
        this.jwtService = jwtService;
    }

    //23.질문에 좋아요하기

    @ResponseBody
    @PostMapping("/questions/creation")
    public BaseResponse<String> createQueLike(@RequestBody PostQueLikeReq postQueLikeReq){

        try{
            String result = questionLikeService.createLike(postQueLikeReq);
            return new BaseResponse<>(result);
        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/replies/creation")
    public BaseResponse<String> createReplyLike(@RequestBody PostReplyLikeReq postReplyLikeReq){

        try{
            String result = questionLikeService.createReplyLike(postReplyLikeReq);
            return new BaseResponse<>(result);
        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
