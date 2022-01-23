package com.mumulcom.mumulcom.src.question.controller;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.src.question.domain.SearchCodingQuestionRes;
import com.mumulcom.mumulcom.src.question.domain.SearchConceptQuestionRes;
import com.mumulcom.mumulcom.src.question.provider.SearchQuestionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions/search")
public class SearchQuestionController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SearchQuestionProvider searchQuestionProvider;

    public SearchQuestionController(SearchQuestionProvider searchQuestionProvider) {
        this.searchQuestionProvider = searchQuestionProvider;
    }

    @ResponseBody
    @GetMapping("/concept/{keyword}")
    public BaseResponse<List<SearchConceptQuestionRes>> searchConceptQuestion(@PathVariable("keyword") String keyword) {
        try {
            List<SearchConceptQuestionRes> searchConceptQuestionRes = searchQuestionProvider.searchConceptQuestionResList(keyword);
            return new BaseResponse<>(searchConceptQuestionRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/coding/{keyword}")
    public BaseResponse<List<SearchCodingQuestionRes>> searchCodingQuestion(@PathVariable("keyword") String keyword) {
        try {
            List<SearchCodingQuestionRes> searchCodingQuestionRes = searchQuestionProvider.searchCodingQuestionResList(keyword);
            return new BaseResponse<>(searchCodingQuestionRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
