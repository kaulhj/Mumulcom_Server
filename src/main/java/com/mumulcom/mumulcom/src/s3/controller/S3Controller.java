package com.mumulcom.mumulcom.src.s3.controller;

import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.src.s3.service.S3UploadService;
import com.mumulcom.mumulcom.src.s3.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/*
@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {

    @Autowired
    private S3Uploader s3Uploader;


    @PostMapping("/images")
    public BaseResponse<List<String>> upload(@RequestParam("images") MultipartFile[] multipartFile)  {
        try {
            List<MultipartFile> fileNames = new ArrayList<>();
            List<String> urlList = new ArrayList<>();
            for(int i=0;i<multipartFile.length;i++) {
                fileNames.add(multipartFile[i]);
                String imagePath1 = s3Uploader.upload(fileNames.get(i), "static");
                urlList.add(imagePath1);
            }
            return new BaseResponse<>(urlList);
        }catch(Exception exception){
            exception.printStackTrace();
            List<String> mylist = Collections.singletonList("이미지 전송 실패");
            return new BaseResponse<>(mylist);
        }

    }




    @ResponseBody
    @GetMapping("/find")
    public BaseResponse<String> findImg() {
        String imgPath = s3UploadService.getThumbnailPath("static/스크린샷(1).png");
        return new BaseResponse<>(imgPath);
        //log.info(imgPath);
        //Assertions.assertThat(imgPath).isNotNull();
    }



*/