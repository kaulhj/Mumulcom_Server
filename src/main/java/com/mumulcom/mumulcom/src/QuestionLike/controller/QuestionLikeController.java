package com.mumulcom.mumulcom.src.QuestionLike.controller;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.src.QuestionLike.dto.PostLikeReq;
import com.mumulcom.mumulcom.src.QuestionLike.provider.QuestionLikeProvider;
import com.mumulcom.mumulcom.src.QuestionLike.service.QuestionLikeService;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/mumulcom.shop/like")

public class QuestionLikeController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final QuestionLikeProvider questionLikeProvider;
    @Autowired
    private final QuestionLikeService questionLikeService;
    @Autowired
    private final JwtService jwtService;

    public QuestionLikeController(QuestionLikeProvider questionLikeProvider, QuestionLikeService questionLikeService,
                                  JwtService jwtService){
        this.questionLikeProvider = questionLikeProvider;
        this.questionLikeService = questionLikeService;
        this.jwtService = jwtService;
    }

    //18.좋아요하기

    @ResponseBody
    @PostMapping("/creation")
    public BaseResponse<String> createLike(@RequestBody PostLikeReq postLikeReq){

        try{
            String result = questionLikeService.createLike(postLikeReq);
            return new BaseResponse<>(result);
        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
