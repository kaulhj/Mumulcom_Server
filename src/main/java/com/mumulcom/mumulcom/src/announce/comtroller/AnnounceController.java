package com.mumulcom.mumulcom.src.announce.comtroller;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.src.announce.model.AnnounceRes;
import com.mumulcom.mumulcom.src.announce.provider.AnnounceProvider;
import com.mumulcom.mumulcom.src.notice.provider.NoticeProvider;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/announce")
public class AnnounceController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final AnnounceProvider announceProvider;
    @Autowired
    private final JwtService jwtService;

    public AnnounceController(AnnounceProvider announceProvider, JwtService jwtService) {
        this.announceProvider = announceProvider;
        this.jwtService = jwtService;
    }

    @GetMapping("")
    public BaseResponse<List<AnnounceRes>> announceList() {
        try{
            List<AnnounceRes> announceResult = announceProvider.getAnnounceList();
            return new BaseResponse<>(announceResult);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
