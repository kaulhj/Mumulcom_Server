package com.mumulcom.mumulcom.src.user.service;

import com.github.dozermapper.core.Mapper;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.src.user.domain.User;
import com.mumulcom.mumulcom.src.user.dto.UserJwtData;
import com.mumulcom.mumulcom.src.user.dto.UserSignInData;
import com.mumulcom.mumulcom.src.user.dto.UserSignUpData;
import com.mumulcom.mumulcom.src.user.repository.UserRepository;
import com.mumulcom.mumulcom.utils.JwtService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

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
     *
     * @param userSignUpData 회원가입 시 필요한 데이터
     * @return User
     */
    public User join(UserSignUpData userSignUpData) {
        User user = mapper.map(userSignUpData, User.class);
        return userRepository.save(user);
    }

    /**
     * 로그인
     * @param userSignInData
     * @return
     */
    public Optional<UserJwtData> login(UserSignInData userSignInData) {
        String email = userSignInData.getEmail();
        if (!isExistEmail(email)) {
            return Optional.empty();
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.get();
        String jwt = jwtService.createJwt(user.getUserIdx());
        UserJwtData userJwtData = UserJwtData.builder()
                .userIdx(user.getUserIdx())
                .jwt(jwt)
                .build();
        return Optional.of(userJwtData);
    }


    /**
     * 이메일 중복 검사
     */
    public boolean isExistEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }
}
