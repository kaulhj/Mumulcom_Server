package com.mumulcom.mumulcom.src.user.service;

import com.github.dozermapper.core.Mapper;
import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import com.mumulcom.mumulcom.src.user.domain.User;
import com.mumulcom.mumulcom.src.user.dto.UserDto.SignUpReq;
import com.mumulcom.mumulcom.src.user.dto.UserDto.SignUpRes;
import com.mumulcom.mumulcom.src.user.repository.UserRepository;
import com.mumulcom.mumulcom.utils.JwtService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.mumulcom.mumulcom.config.BaseResponseStatus.FAILED_TO_LOGIN;
import static com.mumulcom.mumulcom.src.user.dto.UserDto.SignInReq;
import static com.mumulcom.mumulcom.src.user.dto.UserDto.SignInRes;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final Mapper mapper;

    public UserService(UserRepository userRepository, JwtService jwtService, Mapper mapper) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.mapper = mapper;
    }

    /**
     * 회원가입
     */
    public SignUpRes join(SignUpReq signUpReq) throws BaseException {
        if (userRepository.
                existsUserByEmail(signUpReq.getEmail())){
            throw new BaseException(BaseResponseStatus.POST_USERS_EXISTS_EMAIL);
        } else if (userRepository.existsUserByNickname(signUpReq.getNickname())) {
            throw new BaseException(BaseResponseStatus.POST_USERS_EXISTS_NICKNAME);
        }
        User user = userRepository.save(mapper.map(signUpReq, User.class));
        return SignUpRes.builder()
                .userIdx(user.getUserIdx())
                .build();
    }

    /**
     * 로그인
     */
    public SignInRes login(SignInReq signInReq) throws BaseException {
        String email = signInReq.getEmail();
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (userOptional.isEmpty()) {
            throw new BaseException(FAILED_TO_LOGIN);
        }
        User user = userOptional.get();
        String jwt = jwtService.createJwt(user.getUserIdx());
        return SignInRes.builder()
                .jwt(jwt)
                .build();
    }
}
