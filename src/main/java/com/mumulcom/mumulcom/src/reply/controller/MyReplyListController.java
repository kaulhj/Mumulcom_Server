package com.mumulcom.mumulcom.src.reply.controller;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.src.reply.domain.MyReplyListRes;
import com.mumulcom.mumulcom.src.reply.provider.MyReplyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/replies")
public class MyReplyListController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MyReplyProvider myReplyProvider;

    public MyReplyListController(MyReplyProvider myReplyProvider) {
        this.myReplyProvider = myReplyProvider;
    }

    @ResponseBody
    @GetMapping("/my/{userIdx}")
    public BaseResponse<List<MyReplyListRes>> myReplyList(@PathVariable("userIdx") int userIdx) {
        try {
            List<MyReplyListRes> myReplyListRes = myReplyProvider.myReplyListResList(userIdx);
            return new BaseResponse<>(myReplyListRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
