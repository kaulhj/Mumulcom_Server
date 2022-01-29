package com.mumulcom.mumulcom.src.questionlike.provider;


import com.mumulcom.mumulcom.src.questionlike.dao.QuestionLikeDao;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class QuestionLikeProvider {
    private final QuestionLikeDao questionLikeDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired

    public QuestionLikeProvider(QuestionLikeDao questionLikeDao, JwtService jwtService) {
        this.questionLikeDao = questionLikeDao;
        this.jwtService = jwtService;
    }
}
