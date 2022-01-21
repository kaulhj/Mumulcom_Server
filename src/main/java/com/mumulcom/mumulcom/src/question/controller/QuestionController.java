package com.mumulcom.mumulcom.src.question.controller;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.src.question.domain.Question;
import com.mumulcom.mumulcom.src.question.dto.GetQuestionRes;
import com.mumulcom.mumulcom.src.question.service.QuestionService;
import com.mumulcom.mumulcom.src.question.dto.GetCodingQuestionRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    /**
     * 5번 API 코딩 질문 조회
     * [GET] /question/coding/:questionIdx
     */
    @ResponseBody
    @GetMapping("/coding/{questionIdx}")
    public BaseResponse<List<GetCodingQuestionRes>> getCodingQuestions(@PathVariable("questionIdx") int questionIdx) {
        try{
            List<GetCodingQuestionRes> getCodingQuestionRes = questionService.getCodingQuestions(questionIdx);
            return new BaseResponse<>(getCodingQuestionRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * yeji test API
     * [GET] /questions 전체 질문 조회
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<Question>> getQuestions() {
        try {
            List<Question> questions = questionService.findAll();
            return new BaseResponse<>(questions);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * yeji test API
     * [GET] /questions/:quesetionIdx
     */

    @ResponseBody
    @GetMapping("/{questionIdx}")
    public BaseResponse<List<GetQuestionRes>> getQuestion(@PathVariable("questionIdx") int questionIdx) {
        try {
            List<GetQuestionRes> questions = questionService.getQuestions(questionIdx);
            return new BaseResponse<>(questions);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
