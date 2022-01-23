package com.mumulcom.mumulcom.src.scrap.provider;

import com.mumulcom.mumulcom.src.scrap.dao.ScrapDao;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service



public class ScrapProvider {

    private final ScrapDao scrapDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ScrapProvider(ScrapDao scrapDao, JwtService jwtService){
        this.scrapDao = scrapDao;
        this.jwtService = jwtService;
    }
}
