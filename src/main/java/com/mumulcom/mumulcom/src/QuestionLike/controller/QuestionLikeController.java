package com.mumulcom.mumulcom.src.like.controller;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.src.like.dto.PostQueLikeReq;
import com.mumulcom.mumulcom.src.like.dto.PostReplyLikeReq;
import com.mumulcom.mumulcom.src.like.provider.LikeProvider;
import com.mumulcom.mumulcom.src.like.service.LikeService;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/likes")

public class LikeController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final LikeProvider likeProvider;
    @Autowired
    private final LikeService likeService;
    @Autowired
    private final JwtService jwtService;

    public LikeController(LikeProvider likeProvider, LikeService likeService,
                          JwtService jwtService){
        this.likeProvider = likeProvider;
        this.likeService = likeService;
        this.jwtService = jwtService;
    }

    //23.질문에 좋아요하기

    @ResponseBody
    @PostMapping("/questions/creation")
    public BaseResponse<String> createQueLike(@RequestBody PostQueLikeReq postQueLikeReq){

        try{
            String result = likeService.createLike(postQueLikeReq);
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
            String result = likeService.createReplyLike(postReplyLikeReq);
            return new BaseResponse<>(result);
        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
