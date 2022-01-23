package com.mumulcom.mumulcom.src.question.provider;

//import com.example.demo.src.user_1.model.*;
//import com.example.demo.utils.JwtService;
import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import com.mumulcom.mumulcom.config.BaseResponseStatus.*;
import com.mumulcom.mumulcom.src.question.dao.QuestionDao;
import com.mumulcom.mumulcom.src.question.dto.GetRecQueRes;
import com.mumulcom.mumulcom.utils.JwtService;
        import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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



//2.
   public GetRecQueRes getRecentQuestion(long userIdx) throws BaseException {
      try{
         GetRecQueRes getRecQueRes = questionDao.getRecentQuestion(userIdx);
         return getRecQueRes;
      }catch (Exception exception){{
         exception.printStackTrace();
         throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
      }}
   }

   //21.
   public GetRecQueRes getRecQueByPage(long userIdx, int pages) throws BaseException {
      try{
         GetRecQueRes getRecQueRes = questionDao.getRecQueByPage(userIdx,pages);
         return getRecQueRes;
      }catch (Exception exception){{
         exception.printStackTrace();
         throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
      }}
   }

   //20
   public List<GetRecQueRes> getRecQuestions(long userIdx) throws BaseException{
      try{
         List<GetRecQueRes> getRecQueRes = questionDao.getRecQuestions(userIdx);
         return getRecQueRes;
      }catch (Exception exception){
         throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
      }
   }


}
