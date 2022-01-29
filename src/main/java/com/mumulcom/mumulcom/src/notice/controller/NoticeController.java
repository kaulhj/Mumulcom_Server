package com.mumulcom.mumulcom.src.notice.controller;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.src.notice.provider.NoticeProvider;
import com.mumulcom.mumulcom.src.notice.repository.GetNoticeRes;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notices")
public class NoticeController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final NoticeProvider noticeProvider;
    @Autowired
    private final JwtService jwtService;

    public NoticeController(NoticeProvider noticeProvider, JwtService jwtService) {
        this.noticeProvider = noticeProvider;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetNoticeRes>> noticeList(@PathVariable("userIdx") int userIdx) {
        try {
            List<GetNoticeRes> noticeRes = noticeProvider.getNoticeResList(userIdx);
            return new BaseResponse<>(noticeRes);
        }  catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
