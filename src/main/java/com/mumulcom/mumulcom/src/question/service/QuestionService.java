package com.mumulcom.mumulcom.src.question.service;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import com.mumulcom.mumulcom.src.question.dao.QuestionDao;
import com.mumulcom.mumulcom.src.question.domain.Question;
import com.mumulcom.mumulcom.src.question.dto.*;
import com.mumulcom.mumulcom.src.question.provider.QuestionProvider;
import com.mumulcom.mumulcom.src.question.repository.QuestionRepository;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.mumulcom.mumulcom.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional
public class QuestionService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final QuestionDao questionDao;
    private final QuestionProvider questionProvider;
    private final JwtService jwtService;
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionDao questionDao, QuestionProvider questionProvider, QuestionRepository questionRepository,
                           JwtService jwtService){
        this.questionDao = questionDao;
        this.questionProvider = questionProvider;
        this.questionRepository = questionRepository;
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

    /**
     * qustions 전체 조회
     */
    public List<Question> findAll() throws BaseException {
        try {
            return questionRepository.findAll();
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    /**
     * 5번 API 코딩 질문 조회
     */
    public List<GetCodingQuestionRes> getCodingQuestions(int questionIdx) throws BaseException {
        try{
            List<GetCodingQuestionRes> getCodingQuestionRes = questionDao.getCodingQuestions(questionIdx);
            return getCodingQuestionRes;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 6번 API 개념 질문 조회
     */
    public List<GetConceptQuestionRes> getConceptQuestions(int questionIdx) throws BaseException {
        try {
            List<GetConceptQuestionRes> getConceptQuestionRes = questionDao.getConceptQuestions(questionIdx);
            return getConceptQuestionRes;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 7번 API
     * 카테고리별 질문 목록 조회 API
     */
    public List<GetQuestionListRes> getQuestionsByCategory(int sort, int bigCategoryIdx, int smallCategoryIdx) throws BaseException {
        try {
            List<GetQuestionListRes> getQuestionListRes = questionDao.getQuestionsByCategory(sort, bigCategoryIdx, smallCategoryIdx);
            return getQuestionListRes;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * questionIdx를 이용한 특정 질문 조회 test API
     */
    public List<GetQuestionRes> getQuestions(int questionIdx) throws BaseException {
        try {
            List<GetQuestionRes> getQuestionRes = questionDao.getQuestions(questionIdx);
            return getQuestionRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
