package com.mumulcom.mumulcom.src.question.controller;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import com.mumulcom.mumulcom.src.question.domain.MyQuestionListRes;
import com.mumulcom.mumulcom.src.question.domain.Question;

import com.mumulcom.mumulcom.src.question.domain.SearchCodingQuestionRes;
import com.mumulcom.mumulcom.src.question.domain.SearchConceptQuestionRes;
import com.mumulcom.mumulcom.src.question.dto.*;
import com.mumulcom.mumulcom.src.question.provider.QuestionProvider;
import com.mumulcom.mumulcom.src.question.service.QuestionService;
import com.mumulcom.mumulcom.utils.JwtService;
import com.mumulcom.mumulcom.utils.ValidationRegex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/questions")
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





    //학준 7. 코딩질문하기
    @ResponseBody
    @PostMapping("/coding")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> codeQuestion(
            @RequestPart(value = "images", required = false) List<MultipartFile> multipartFile,
            @RequestPart(value = "codeQuestionReq") CodeQuestionReq codeQuestionReq){
        //s3이미지 저장하고 url반환값
        try {
            Long userIdxByJwt = jwtService.getUserIdx();
            if (!userIdxByJwt.equals(codeQuestionReq.getUserIdx())) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }
                if(codeQuestionReq.getUserIdx() == 0 || codeQuestionReq.getCurrentError() == null
                         || codeQuestionReq.getBigCategoryIdx() == 0
                         || codeQuestionReq.getTitle() == null) {
                    throw new BaseException(BaseResponseStatus.POST_EMPTY_ESSENTIAL_BODY);
                }

                //카테고리 범위 검사(1~5,1~12)
                if(!ValidationRegex.bigCategoryRange(Long.toString(codeQuestionReq.getBigCategoryIdx()))
                ) {
                    throw new BaseException(BaseResponseStatus.POST_QUESTIONS_INVALID_CATEGORY_RANGE);
                }
                if(codeQuestionReq.getSmallCategoryIdx()!= 0)
                    if(!ValidationRegex.smallCategoryRange(Long.toString(codeQuestionReq.getSmallCategoryIdx())))
                        throw new BaseException(BaseResponseStatus.POST_QUESTIONS_INVALID_CATEGORY_RANGE);
                List<String> imageUrls = null;
                if(! multipartFile.get(0).getOriginalFilename().equals(""))
                    imageUrls = questionService.uploadS3image(multipartFile, codeQuestionReq.getUserIdx());
                else
                    imageUrls = new ArrayList<>();
                String result = questionService.codeQuestion(imageUrls, codeQuestionReq);
                return new BaseResponse<>(result);
                }catch (BaseException exception) {
                    exception.printStackTrace();
                    return new BaseResponse<>(exception.getStatus());
            }
        }


    //학준 8.개념질문하기
    @ResponseBody
    @PostMapping("/concept")
    public BaseResponse<String> conceptQuestion(
            @RequestPart(value = "images", required = false ) List<MultipartFile> multipartFile,
            @RequestPart(value = "conceptQueReq") ConceptQueReq conceptQueReq){
        try{
            Long userIdxByJwt = jwtService.getUserIdx();
            if (!userIdxByJwt.equals(conceptQueReq.getUserIdx())) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }
            if(conceptQueReq.getUserIdx() == 0 || conceptQueReq.getContent() == null
                    || conceptQueReq.getBigCategoryIdx() == 0
                    || conceptQueReq.getTitle() == null) {
                throw new BaseException(BaseResponseStatus.POST_EMPTY_ESSENTIAL_BODY);
            }
            if(conceptQueReq.getSmallCategoryIdx()!= 0)
                if(!ValidationRegex.smallCategoryRange(Long.toString(conceptQueReq.getSmallCategoryIdx())))
                    throw new BaseException(BaseResponseStatus.POST_QUESTIONS_INVALID_CATEGORY_RANGE);
            if(!ValidationRegex.bigCategoryRange(Long.toString(conceptQueReq.getBigCategoryIdx()))
                    || !ValidationRegex.smallCategoryRange(Long.toString(conceptQueReq.getSmallCategoryIdx()))){
                throw new BaseException(BaseResponseStatus.POST_QUESTIONS_INVALID_CATEGORY_RANGE);
            }
            List<String> imageUrls = questionService.uploadS3image(multipartFile, conceptQueReq.getUserIdx());
            String result = questionService.conceptQuestion(imageUrls, conceptQueReq);

            return new BaseResponse<>(result);
        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //학준 9. 나의 최근 질문 최대 4개(전체 개수만큼 반환, 로그인 시 메인 화면 스크롤 기능)
    @ResponseBody
    @GetMapping("/latest/{userIdx}")    //유저인덱스 유효성은 jwt로
    public BaseResponse<List<GetRecQueRes>> getRecQuestion(@PathVariable("userIdx")long userIdx
    ){
        try{
            Long userIdxByJwt = jwtService.getUserIdx();
            if (!userIdxByJwt.equals(userIdx)) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }

            List<GetRecQueRes> getRecQueRes = questionProvider.getRecQuestion(userIdx);
            return new BaseResponse<>(getRecQueRes);
        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }

    //학준 16.나의 답변달린 질문 전체 조회
    @ResponseBody
    @GetMapping("/my/reply/{userIdx}")
    public BaseResponse<List<GetRecQueRes>> getRecQuestions(@PathVariable("userIdx")long userIdx
    ){
        try{
            Long userIdxByJwt = jwtService.getUserIdx();
            if (!userIdxByJwt.equals(userIdx)) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }

            List<GetRecQueRes> getRecQueRes = questionProvider.getRecQuestions(userIdx);
            return new BaseResponse<>(getRecQueRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * yeji 10번 API 코딩 질문 조회
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
     * yeji 11번 API 개념 질문 조회
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
     * yeji 12번 API 카테고리별 질문 목록 조회
     * [GET] /questions?type=&sort=&bigCategory=&smallCategory=&isReplied=&lastQuestionIdx=&perPage=
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetQuestionListRes>> getQuestionsList(@RequestParam(defaultValue = "1") int type , @RequestParam int sort, @RequestParam int bigCategoryIdx, @RequestParam(required = false, defaultValue = "0") int smallCategoryIdx, @RequestParam(defaultValue = "true") boolean isReplied,
                                                                    @RequestParam int lastQuestionIdx, @RequestParam int perPage) {
        try {
            List<GetQuestionListRes> getQuestionListRes = questionService.getQuestionsByCategory(type, sort, bigCategoryIdx, smallCategoryIdx, isReplied, lastQuestionIdx, perPage);
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
     * 개념 질문 검색 API
     * [GET] /questions/search/concept?keyword=
     * */
    @ResponseBody
    @GetMapping("/search/concept")
    public BaseResponse<List<SearchConceptQuestionRes>> searchConceptQuestion(@RequestParam(required = false) String keyword) {
        try {
            List<SearchConceptQuestionRes> searchConceptQuestionRes;
            if(keyword == null) {
                searchConceptQuestionRes = questionProvider.searchConceptQuestionResList();
            } else {
                searchConceptQuestionRes = questionProvider.searchConceptQuestionResList(keyword);
            }
            return new BaseResponse<>(searchConceptQuestionRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 휘정
     * 코딩 질문 검색 API
     * [GET] /questions/search/coding?keyword=
     * */
    @ResponseBody
    @GetMapping("/search/coding")
    public BaseResponse<List<SearchCodingQuestionRes>> searchCodingQuestion(@RequestParam(required = false) String keyword) {
        try {
            List<SearchCodingQuestionRes> searchCodingQuestionRes;
            if(keyword == null) {
                searchCodingQuestionRes = questionProvider.searchCodingQuestionResList();
            } else {
                searchCodingQuestionRes = questionProvider.searchCodingQuestionResList(keyword);
            }
            return new BaseResponse<>(searchCodingQuestionRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 휘정
     * 내가 한 코딩 질문 조회 API
     * [GET] /questions/my/coding/...( 쿼리스트링)
     * 일단 모든 질문 다 보여주고 isreplied가 true가 되면 답변달린것만 보여주기
     * */
    @ResponseBody
    @GetMapping("/my/coding")
    public BaseResponse<List<MyQuestionListRes>> myCodingQuestion(@RequestParam long userIdx, @RequestParam(defaultValue = "false") boolean isReplied) {
        try {

            Long userIdxByJwt = jwtService.getUserIdx();
            if (!userIdxByJwt.equals(userIdx)) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }

            List<MyQuestionListRes> myQuestionListRes = questionProvider.myCodingQuestionListResList(userIdx, isReplied);
            return new BaseResponse<>(myQuestionListRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 휘정
     * 내가 한 개념 질문 조회 API
     * [GET] /questions/my/concept/...( 쿼리스트링)
     * 일단 모든 질문 다 보여주고 isreplied가 true가 되면 답변달린것만 보여주기
     * */
    @ResponseBody
    @GetMapping("/my/concept")
    public BaseResponse<List<MyQuestionListRes>> myConceptQuestion(@RequestParam long userIdx, @RequestParam(defaultValue = "false") boolean isReplied) {
        try {

            Long userIdxByJwt = jwtService.getUserIdx();
            if (!userIdxByJwt.equals(userIdx)) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }

            List<MyQuestionListRes> myQuestionListRes = questionProvider.myConceptQuestionListResList(userIdx, isReplied);
            return new BaseResponse<>(myQuestionListRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
