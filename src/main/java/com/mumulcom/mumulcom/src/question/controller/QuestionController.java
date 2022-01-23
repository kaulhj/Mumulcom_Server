package com.mumulcom.mumulcom.src.question.controller;

import com.mumulcom.mumulcom.config.*;
import com.mumulcom.mumulcom.src.question.dto.CodeQuestionReq;
import com.mumulcom.mumulcom.src.question.dto.ConceptQueReq;
import com.mumulcom.mumulcom.src.question.dto.GetRecQueRes;
import com.mumulcom.mumulcom.src.question.provider.QuestionProvider;
import com.mumulcom.mumulcom.src.question.service.QuestionService;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/mumulcom.shop")


public class QuestionController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final QuestionProvider questionProvider;
    @Autowired
    private final QuestionService questionService;
    @Autowired
    private final JwtService jwtService;

    public QuestionController(QuestionProvider questionprovider, QuestionService questionService,
                              JwtService jwtService){
        this.questionProvider = questionprovider;
        this.questionService = questionService;
        this.jwtService = jwtService;
    }



    //2. 유저의 최근(7일 이내)  질문등 조회 .우선적으로 하나 조회(다중은 좀더 고민..)
    @ResponseBody
    @GetMapping("/questions/my/home/{userIdx}")
    public BaseResponse<GetRecQueRes> getRecentQuestion(@PathVariable("userIdx")long userIdx){
        try{
            GetRecQueRes getRecQueRes = questionProvider.getRecentQuestion(userIdx);
            return new BaseResponse<>(getRecQueRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }

    //20.유저의 최근(7일이내) 질문등 조회
    @ResponseBody
    @GetMapping("/questions/my/latest/{userIdx}")
    public BaseResponse<List<GetRecQueRes>> getRecQuestions(@PathVariable("userIdx")long userIdx){
        try{
            List<GetRecQueRes> getRecQueRes = questionProvider.getRecQuestions(userIdx);
            return new BaseResponse<>(getRecQueRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //3. 코딩질문하기
    @ResponseBody
    @PostMapping("/questions/coding")
    public BaseResponse<String> codeQuestion( @RequestBody CodeQuestionReq codeQuestionReq){

        try{
            String result = questionService.codeQuestion( codeQuestionReq);

            return new BaseResponse<>(result);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //4.개념질문하기
    @ResponseBody
    @PostMapping("/questions/concept")
    public BaseResponse<String> conceptQuestion(@RequestBody ConceptQueReq conceptQuestion){

        try{
            String result = questionService.conceptQuestion(conceptQuestion);

            return new BaseResponse<>(result);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }




}
