package com.mumulcom.mumulcom.src.scrap.controller;


import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import com.mumulcom.mumulcom.src.scrap.dto.PostScrapReq;
import com.mumulcom.mumulcom.src.scrap.provider.ScrapProvider;
import com.mumulcom.mumulcom.src.scrap.service.ScrapService;
import com.mumulcom.mumulcom.src.user.service.UserService;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/mumulcom.shop/scrap")

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


    //17. 스크랩하기 / 내글은 스크랩 못하게 밸리데이션
    @ResponseBody
    @PostMapping("/creation")
    public BaseResponse<String> createScrap(@RequestBody PostScrapReq postScrapReq){

        try{
            String result = scrapService.createScrap(postScrapReq);
            return new BaseResponse<>(result);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
