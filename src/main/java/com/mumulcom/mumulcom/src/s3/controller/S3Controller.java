package com.mumulcom.mumulcom.src.s3.controller;

import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.src.s3.service.S3UploadService;
import com.mumulcom.mumulcom.src.s3.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController

@RequestMapping("/mumulcom.shop")

public class S3Controller {

    @Autowired
    private S3UploadService s3UploadService;

/*
    @PostMapping("/images")
    public String upload(@RequestParam("images") MultipartFile multipartFile)  {
     try {
         s3Uploader.upload(multipartFile, "static");
         return "test";
     }catch (IOException exception){
         exception.printStackTrace();
         return new String("이미지 업로드 실패");
     }
    }


 */


    @ResponseBody
    @GetMapping("/find")
    public BaseResponse<String> findImg() {
        String imgPath = s3UploadService.getThumbnailPath("스크린샷(2).png");
        return new BaseResponse<>(imgPath);
        //log.info(imgPath);
        //Assertions.assertThat(imgPath).isNotNull();
    }
}
