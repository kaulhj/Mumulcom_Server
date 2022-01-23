package com.mumulcom.mumulcom.src.question.controller;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.src.question.domain.MyQuestionListRes;
import com.mumulcom.mumulcom.src.question.provider.MyQuestionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions/my")
public class MyQuestionListController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MyQuestionProvider myQuestionProvider;

    public MyQuestionListController(MyQuestionProvider myQuestionProvider) {
        this.myQuestionProvider = myQuestionProvider;
    }

    @ResponseBody
    @GetMapping("{userIdx}")
    public BaseResponse<List<MyQuestionListRes>> myQuestion(@PathVariable("userIdx") int userIdx) {
        try {
            List<MyQuestionListRes> myQuestionListRes = myQuestionProvider.myQuestionListResList(userIdx);
            return new BaseResponse<>(myQuestionListRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
