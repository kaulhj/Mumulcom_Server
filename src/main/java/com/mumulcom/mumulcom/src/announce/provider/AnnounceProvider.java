package com.mumulcom.mumulcom.src.announce.provider;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.src.announce.dao.AnnounceDao;
import com.mumulcom.mumulcom.src.announce.model.AnnounceRes;
import com.mumulcom.mumulcom.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mumulcom.mumulcom.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class AnnounceProvider {
    private final AnnounceDao announceDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AnnounceProvider (AnnounceDao announceDao, JwtService jwtService) {
        this.announceDao = announceDao;
        this.jwtService = jwtService;
    }

    public List<AnnounceRes> getAnnounceList() throws BaseException {
        try {
            List<AnnounceRes> getAnnounce = announceDao.getAnnounce();
            return getAnnounce;
        } catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public AnnounceRes getAnnounceById(long announceIdx) throws BaseException {
        try {
            AnnounceRes getAnnounce = announceDao.getAnnounce(announceIdx);
            return getAnnounce;
        } catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
