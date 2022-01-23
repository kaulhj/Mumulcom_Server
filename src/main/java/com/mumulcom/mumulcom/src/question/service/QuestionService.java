package com.mumulcom.mumulcom.src.question.service;


import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import com.mumulcom.mumulcom.src.question.dao.QuestionDao;
import com.mumulcom.mumulcom.src.question.dto.CodeQuestionReq;
import com.mumulcom.mumulcom.src.question.dto.ConceptQueReq;
import com.mumulcom.mumulcom.src.question.provider.QuestionProvider;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final QuestionDao questionDao;
    private final QuestionProvider questionProvider;
    private final JwtService jwtService;

    @Autowired
    public QuestionService(QuestionDao questionDao, QuestionProvider questionProvider,
                           JwtService jwtService){
        this.questionDao = questionDao;
        this.questionProvider = questionProvider;
        this.jwtService = jwtService;

    }

    public String codeQuestion(CodeQuestionReq codeQuestionReq)throws BaseException{
        try{
            String result = questionDao.codeQuestion(codeQuestionReq);
            return result;
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public String conceptQuestion(ConceptQueReq conceptQueReq)throws BaseException{
        try{
            String result = questionDao.conceptQuestion(conceptQueReq);
            return result;
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

}
