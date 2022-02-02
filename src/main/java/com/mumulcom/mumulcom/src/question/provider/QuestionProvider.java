package com.mumulcom.mumulcom.src.question.provider;

//import com.example.demo.src.user_1.model.*;
//import com.example.demo.utils.JwtService;
import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import com.mumulcom.mumulcom.config.BaseResponseStatus.*;
import com.mumulcom.mumulcom.src.question.dao.QuestionDao;
import com.mumulcom.mumulcom.src.question.domain.MyQuestionListRes;
import com.mumulcom.mumulcom.src.question.domain.SearchCodingQuestionRes;
import com.mumulcom.mumulcom.src.question.domain.SearchConceptQuestionRes;
import com.mumulcom.mumulcom.src.question.dto.GetRecQueRes;
import com.mumulcom.mumulcom.utils.JwtService;
        import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mumulcom.mumulcom.config.BaseResponseStatus.DATABASE_ERROR;
//import com.example.demo.utils.*;
//import com.example.demo.config.secret.*;


@Service
public class QuestionProvider {

   private final QuestionDao questionDao;
   private final JwtService jwtService;

   final Logger logger = LoggerFactory.getLogger(this.getClass());

   @Autowired
    public QuestionProvider(QuestionDao questionDao, JwtService jwtService){
       this.questionDao = questionDao;
       this.jwtService = jwtService;
   }



//학준 9. 최근질문 최대 4개 조회
public List<GetRecQueRes> getRecQuestion(long userIdx) throws BaseException{
      int countSize = questionDao.countSize(userIdx);
      if(countSize == 0)
         throw new BaseException(BaseResponseStatus.GET_USERS_EMPTY_DATA);
      try{
      List<GetRecQueRes> getRecQueRes = questionDao.getRecQuestion(countSize, userIdx);
      return getRecQueRes;
   }catch (Exception exception){
      exception.printStackTrace();
      throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
   }
}




   //학준 16
   public List<GetRecQueRes> getRecQuestions(long userIdx) throws BaseException{

         int countSize = questionDao.getRecQueSize(userIdx);
         //유저에 관한 정보가 0개일때
         if(countSize == 0)
            throw new BaseException(BaseResponseStatus.GET_USERS_EMPTY_DATA);
      try{
         List<GetRecQueRes> getRecQueRes = questionDao.getRecQuestions(countSize, userIdx);
         return getRecQueRes;
      }catch (Exception exception){
         exception.printStackTrace();
         throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
      }
   }

   /**
    * 휘정
    * 개념 질문 검색하기
    * */
   public List<SearchConceptQuestionRes> searchConceptQuestionResList(String keyword) throws BaseException {
      try {
         List<SearchConceptQuestionRes> searchConceptQuestionResList = questionDao.searchConceptQuestionRes(keyword);
         return searchConceptQuestionResList;
      } catch (Exception exception) {
         exception.printStackTrace();
         throw new BaseException(DATABASE_ERROR);
      }
   }

   /**
    * 휘정
    * 코딩 질문 검색하기
    * */
   public List<SearchCodingQuestionRes> searchCodingQuestionResList(String keyword) throws BaseException {
      try {
         List<SearchCodingQuestionRes> searchCodingQuestionResList = questionDao.searchCodingQuestionRes(keyword);
         return searchCodingQuestionResList;
      } catch (Exception exception) {
         exception.printStackTrace();
         throw new BaseException(DATABASE_ERROR);
      }
   }

   /**
    * 휘정
    * 내가 한 질문 목록 받아오는 API
    * */

   // 코딩 질문 목록
   public List<MyQuestionListRes> myCodingQuestionListResList (int userIdx, boolean isReplied) throws BaseException {
      try {
         List<MyQuestionListRes> myQuestionListResList = questionDao.myCodingQuestionListRes(userIdx, isReplied);
         return myQuestionListResList;
      } catch (Exception exception) {
         exception.printStackTrace();
         throw new BaseException(DATABASE_ERROR);
      }
   }

   // 개념 질문 목록
   public List<MyQuestionListRes> myConceptQuestionListResList (int userIdx, boolean isReplied) throws BaseException {
      try {
         List<MyQuestionListRes> myQuestionListResList = questionDao.myConceptQuestionListRes(userIdx,isReplied);
         return myQuestionListResList;
      } catch (Exception exception) {
         exception.printStackTrace();
         throw new BaseException(DATABASE_ERROR);
      }
   }

}
