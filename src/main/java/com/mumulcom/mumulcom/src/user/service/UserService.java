package com.mumulcom.mumulcom.src.user.service;

import com.github.dozermapper.core.Mapper;
import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import com.mumulcom.mumulcom.src.user.domain.User;
import com.mumulcom.mumulcom.src.user.dto.UserDto;
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
     */
    public UserDto.SignUpRes join(UserDto.SignUpReq signUpReq) throws BaseException {
        if (userRepository.existsUserByEmail(signUpReq.getEmail())) {
            throw new BaseException(BaseResponseStatus.POST_USERS_EXISTS_EMAIL);
        } else if (userRepository.existsUserByNickname(signUpReq.getNickname())) {
            throw new BaseException(BaseResponseStatus.POST_USERS_EXISTS_NICKNAME);
        }
        User mappedUser = mapper.map(signUpReq, User.class);
        mappedUser.updateProfileImgUrlRandomly();
        User user = userRepository.save(mappedUser);
        return UserDto.SignUpRes.builder()
                .userIdx(user.getUserIdx())
                .email(user.getEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .profileImgUrl(user.getProfileImgUrl())
                .build();
    }

    /**
     * 로그인
     */
    public UserDto.SignInRes login(UserDto.SignInReq signInReq) throws BaseException {
        String email = signInReq.getEmail();
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (userOptional.isEmpty()) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_LOGIN);
        }
        User user = userOptional.get();
        String jwt = jwtService.createJwt(user.getUserIdx());
        return UserDto.SignInRes.builder()
                .jwt(jwt)
                .userIdx(user.getUserIdx())
                .email(user.getEmail())
                .name(user.getName())
                .nickname(user.getName())
                .group(user.getGroup())
                .profileImgUrl(user.getProfileImgUrl())
                .build();
    }

    /**
     * 회원정보 조회
     */
    public UserDto.UserRes getUser(Long userIdx) throws BaseException {
        Optional<User> userOptional = userRepository.findById(userIdx);
        if (userOptional.isEmpty()) {
            throw new BaseException(BaseResponseStatus.RESPONSE_ERROR);
        }
        User user = userOptional.get();
        return UserDto.UserRes.builder()
                .email(user.getEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .group(user.getGroup())
                .profileImgUrl(user.getProfileImgUrl())
                .build();
    }

    /**
     * 회원정보 수정
     */
    public UserDto.UserRes updateUser(UserDto.PatchReq patchReq) throws BaseException {
        Optional<User> userOptional = userRepository.findById(patchReq.getUserIdx());
        if (userOptional.isEmpty()) {
            throw new BaseException(BaseResponseStatus.RESPONSE_ERROR);
        }
        User user = userOptional.get();
        user.updateUserInfo(
                patchReq.getNickname(),
                patchReq.getGroup(),
                patchReq.getProfileImgUrl()
        );
        return UserDto.UserRes.builder()
                .email(user.getEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .group(user.getGroup())
                .profileImgUrl(user.getProfileImgUrl())
                .build();
    }

    /**
     * 회원 탈퇴
     */
    public UserDto.UserRes deleteUser(Long userIdx) throws BaseException{
        Optional<User> userOptional = userRepository.findById(userIdx);
        if (userOptional.isEmpty()) {
            throw new BaseException(BaseResponseStatus.RESPONSE_ERROR);
        }
        User user = userOptional.get();
        user.deleteUser();
        return UserDto.UserRes.builder()
                .email(user.getEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .group(user.getGroup())
                .profileImgUrl(user.getProfileImgUrl())
                .build();
    }

    /**
     * 닉네임 중복 여부 확인
     */
    public boolean existsByNickname(String nickname) {
        return userRepository.existsUserByNickname(nickname);
    }
}
