package com.mumulcom.mumulcom.src.question.controller;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.src.question.domain.MyQuestionListRes;
import com.mumulcom.mumulcom.src.question.domain.Question;
<<<<<<< HEAD
=======
import com.mumulcom.mumulcom.src.question.domain.SearchCodingQuestionRes;
import com.mumulcom.mumulcom.src.question.domain.SearchConceptQuestionRes;
>>>>>>> b8a10444ee28353b5b1cbc7652aa724f8fa3ac8f
import com.mumulcom.mumulcom.src.question.dto.*;
import com.mumulcom.mumulcom.src.question.provider.QuestionProvider;
import com.mumulcom.mumulcom.src.question.service.QuestionService;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("mumulcom.shop/questions")
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



    //2.21 유저의 최근(7일 이내)  질문등 조회/....옆으로 스크롤 시 그 이전 질문 보이기(4개까지)
    @ResponseBody
    @GetMapping("/my/home/{userIdx}")
    public BaseResponse<GetRecQueRes> getRecentQuestion(@PathVariable("userIdx")long userIdx,
                                                        @RequestParam(required = false) String page){
        try{
            if(page !=null){
                int pages = Integer.parseInt(page)-1;
                GetRecQueRes getRecQueRes = questionProvider.getRecQueByPage(userIdx, pages);
                return new BaseResponse<>(getRecQueRes);
            }
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            GetRecQueRes getRecQueRes = questionProvider.getRecentQuestion(userIdx);
            return new BaseResponse<>(getRecQueRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }

    //20.유저의 최근(7일이내) 질문등 조회 /
    @ResponseBody
    @GetMapping("/my/latest/{userIdx}")
    public BaseResponse<List<GetRecQueRes>> getRecQuestions(@PathVariable("userIdx")long userIdx
                                                       ){
        try{
            List<GetRecQueRes> getRecQueRes = questionProvider.getRecQuestions(userIdx);
            return new BaseResponse<>(getRecQueRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //3. 코딩질문하기
    @ResponseBody
    @PostMapping("/coding")
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
    @PostMapping("/concept")
    public BaseResponse<String> conceptQuestion(@RequestBody ConceptQueReq conceptQuestion){

        try{
            String result = questionService.conceptQuestion(conceptQuestion);

            return new BaseResponse<>(result);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 5번 API 코딩 질문 조회
     * [GET] /questions/coding/:questionIdx
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
     * 6번 API 개념 질문 조회
     * [GET] /questions/concept/:questionIdx
     */
    @ResponseBody
    @GetMapping("/concept/{questionIdx}")
    public BaseResponse<List<GetConceptQuestionRes>> getConceptQuestions(@PathVariable("questionIdx") int questionIdx) {
        try {
            List<GetConceptQuestionRes> getConceptQuestionRes = questionService.getConceptQuestions(questionIdx);
            return new BaseResponse<>(getConceptQuestionRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 7번 API 카테고리별 질문 목록 조회
     * [GET] /questions/?sort=&?bigCategory=&?smallCategory
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetQuestionListRes>> getQuestionsList(@RequestParam int sort, @RequestParam int bigCategoryIdx, @RequestParam(required = false, defaultValue = "0") int smallCategoryIdx) {
        try {
            List<GetQuestionListRes> getQuestionListRes = questionService.getQuestionsByCategory(sort, bigCategoryIdx, smallCategoryIdx);
            return new BaseResponse<>(getQuestionListRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * yeji test API
     * [GET] /questions 전체 질문 조회
     */
    @ResponseBody
    @GetMapping("/test")
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

    /**
     * 휘정
     * 개념 질문 조회 API
     * [GET] /questions/search/concept/:keyword
     * */
    @ResponseBody
    @GetMapping("/search/concept/{keyword}")
    public BaseResponse<List<SearchConceptQuestionRes>> searchConceptQuestion(@PathVariable("keyword") String keyword) {
        try {
            List<SearchConceptQuestionRes> searchConceptQuestionRes = questionProvider.searchConceptQuestionResList(keyword);
            return new BaseResponse<>(searchConceptQuestionRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 휘정
     * 코딩 질문 조회 API
     * [GET] /questions/search/coding/:keyword
     * */
    @ResponseBody
    @GetMapping("/search/coding/{keyword}")
    public BaseResponse<List<SearchCodingQuestionRes>> searchCodingQuestion(@PathVariable("keyword") String keyword) {
        try {
            List<SearchCodingQuestionRes> searchCodingQuestionRes = questionProvider.searchCodingQuestionResList(keyword);
            return new BaseResponse<>(searchCodingQuestionRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 휘정
     * 내가 한 질문 목록 조회 API
     * [GET] /questions/my/:userIdx
     * */
    @ResponseBody
    @GetMapping("/my/{userIdx}")
    public BaseResponse<List<MyQuestionListRes>> myQuestion(@PathVariable("userIdx") int userIdx) {
        try {
            List<MyQuestionListRes> myQuestionListRes = questionProvider.myQuestionListResList(userIdx);
            return new BaseResponse<>(myQuestionListRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
