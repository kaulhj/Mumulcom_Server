package com.mumulcom.mumulcom.src.category.controller;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.src.category.dto.GetCategoryRes;
import com.mumulcom.mumulcom.src.category.provider.CategoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CategoryProvider getCategoryListProvider;

    public CategoryController(CategoryProvider getCategoryListProvider) {
        this.getCategoryListProvider = getCategoryListProvider;
    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetCategoryRes>> getCategory() {
        try {
            List<GetCategoryRes> getCategoryRes = getCategoryListProvider.getCategoryResList();
            return new BaseResponse<> (getCategoryRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
