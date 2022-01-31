package com.mumulcom.mumulcom.src.question.service;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import com.mumulcom.mumulcom.src.question.dao.QuestionDao;
import com.mumulcom.mumulcom.src.question.domain.Question;
import com.mumulcom.mumulcom.src.question.dto.*;
import com.mumulcom.mumulcom.src.question.provider.QuestionProvider;
import com.mumulcom.mumulcom.src.question.repository.QuestionRepository;
import com.mumulcom.mumulcom.src.s3.service.S3Uploader;
import com.mumulcom.mumulcom.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mumulcom.mumulcom.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final QuestionDao questionDao;
    private final QuestionProvider questionProvider;
    private final JwtService jwtService;
    private final QuestionRepository questionRepository;
    private final S3Uploader s3Uploader;


// 7. 코딩질문
    public String codeQuestion(List<String> imgUrls, CodeQuestionReq codeQuestionReq)throws BaseException{
        try{
            String result = questionDao.codeQuestion(imgUrls, codeQuestionReq);
            return result;
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public String conceptQuestion(List<String> imgUrls, ConceptQueReq conceptQueReq)throws BaseException{
        try{
            String result = questionDao.conceptQuestion(imgUrls, conceptQueReq);
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
    public List<GetQuestionListRes> getQuestionsByCategory(int sort, int bigCategoryIdx, int smallCategoryIdx, boolean isReplied, int lastQuestionIdx, int perPage) throws BaseException {
        try {
            List<GetQuestionListRes> getQuestionListRes = questionDao.getQuestionsByCategory(sort, bigCategoryIdx, smallCategoryIdx, isReplied, lastQuestionIdx, perPage);
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


    //7-1. S3이미지업로드
    public List<String> uploadS3image(List<MultipartFile> multipartFileList, Long userIdx) {
        try {

            List<String> imagePath1 = s3Uploader.upload(multipartFileList, "userIdx"+ String.valueOf(userIdx));
            return imagePath1;
        }catch(Exception exception){
            exception.printStackTrace();
            List<String> mylist = Collections.singletonList("이미지 전송 실패");
            return mylist;
        }


    }
}
