package com.mumulcom.mumulcom.src.scrap.controller;


import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import com.mumulcom.mumulcom.src.scrap.domain.MyScrapListRes;
import com.mumulcom.mumulcom.src.scrap.dto.PostScrapReq;
import com.mumulcom.mumulcom.src.scrap.provider.ScrapProvider;
import com.mumulcom.mumulcom.src.scrap.service.ScrapService;
import com.mumulcom.mumulcom.src.user.service.UserService;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/scraps")

public class ScrapController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ScrapProvider scrapProvider;
    @Autowired
    private final ScrapService scrapService;
    @Autowired
    private final JwtService jwtService;

    public ScrapController(ScrapProvider scrapProvider, ScrapService scrapService,
                           JwtService jwtService){
        this.scrapProvider = scrapProvider;
        this.scrapService = scrapService;
        this.jwtService = jwtService;
    }


    //학준 24. 스크랩하기 / 내글은 스크랩 못하게 밸리데이션
    @ResponseBody
    @PostMapping("/creation")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> createScrap(@RequestBody PostScrapReq postScrapReq){

        try{
            Long userIdxByJwt = jwtService.getUserIdx();
            if (!userIdxByJwt.equals(postScrapReq.getUserIdx())) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }

            if( postScrapReq.getQuestionIdx() == null   && postScrapReq.getUserIdx() == null )
                throw new BaseException(BaseResponseStatus.POST_EMPTY_ESSENTIAL_BODY);
            String result = scrapService.createScrap(postScrapReq);
            return new BaseResponse<>(result);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 스크랩한 코딩 질문 목록 모두 보여주기
    // 카테고리 없을 때
    // 큰 카테고리만 있을 때
    // 둘다 있을 때
    // 큰 x, 작은 o 일때는 벨리데이션처리 해주기
    @ResponseBody
    @GetMapping("/coding")
    public BaseResponse<List<MyScrapListRes>> getMyCodingScrapList (@RequestParam long userIdx,  @RequestParam(defaultValue = "false") boolean isReplied,@RequestParam(required = false) String bigCategory, @RequestParam(required = false) String smallCategory) {
        try {
            Long userIdxByJwt = jwtService.getUserIdx();
            if (!userIdxByJwt.equals(userIdx)) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }
            List<MyScrapListRes> myScrapListResList;
            //카테고리 없을 때 -> 스크랩한 목록 다 보여주기
            if(bigCategory == null && smallCategory == null) {
                myScrapListResList = scrapProvider.myCodingScrapListRes(userIdx, isReplied);
            } else if (smallCategory == null) { // 큰 카테고리만 있을 때
                myScrapListResList = scrapProvider.myCodingScrapListRes(userIdx,bigCategory, isReplied);
            } else { // 모두 있을 때
                myScrapListResList = scrapProvider.myCodingScrapListRes(userIdx, bigCategory, smallCategory, isReplied);
            }
            return new BaseResponse<>(myScrapListResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 스크랩한 개념 질문 목록 모두 보여주기
    // 카테고리 없을 때
    // 큰 카테고리만 있을 때
    // 둘다 있을 때
    // 큰 x, 작은 o 일때는 벨리데이션처리 해주기
    @ResponseBody
    @GetMapping("/concept")
    public BaseResponse<List<MyScrapListRes>> getMyConceptScrapList (@RequestParam long userIdx,  @RequestParam(defaultValue = "false") boolean isReplied, @RequestParam(required = false) String bigCategory, @RequestParam(required = false) String smallCategory) {
        try {
            Long userIdxByJwt = jwtService.getUserIdx();
            if (!userIdxByJwt.equals(userIdx)) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }
            List<MyScrapListRes> myScrapListResList;
            //카테고리 없을 때 -> 스크랩한 목록 다 보여주기
            if(bigCategory == null && smallCategory == null) {
                myScrapListResList = scrapProvider.myConceptScrapListRes(userIdx, isReplied);
            } else if (smallCategory == null) { // 큰 카테고리만 있을 때
                myScrapListResList = scrapProvider.myConceptScrapListRes(userIdx,bigCategory, isReplied);
            } else { // 모두 있을 때
                myScrapListResList = scrapProvider.myConceptScrapListRes(userIdx, bigCategory, smallCategory, isReplied);
            }
            return new BaseResponse<>(myScrapListResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
