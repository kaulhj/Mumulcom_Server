package com.mumulcom.mumulcom.src.user.controller;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.config.BaseResponseStatus;
import com.mumulcom.mumulcom.src.user.dto.UserDto;
import com.mumulcom.mumulcom.src.user.dto.UserDto.SignInReq;
import com.mumulcom.mumulcom.src.user.dto.UserDto.SignInRes;
import com.mumulcom.mumulcom.src.user.dto.UserDto.SignUpReq;
import com.mumulcom.mumulcom.src.user.dto.UserDto.SignUpRes;
import com.mumulcom.mumulcom.src.user.service.UserService;
import com.mumulcom.mumulcom.utils.JwtService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원가입 API
     */
    @PostMapping
    public BaseResponse<SignUpRes> createUser(@RequestBody @Valid SignUpReq signUpReq) {
        try {
            SignUpRes result = userService.join(signUpReq);
            return new BaseResponse<>(result);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 로그인 API
     */
    @PostMapping("/login")
    public BaseResponse<SignInRes> login(@RequestBody @Valid SignInReq signInReq) {
        try {
            SignInRes result = userService.login(signInReq);
            return new BaseResponse<>(result);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 회원정보 조회 API
     */
    @GetMapping("/{userIdx}")
    public BaseResponse<UserDto.UserRes> getUser(@PathVariable Long userIdx) {
        try {
            UserDto.UserRes result = userService.getUser(userIdx);
            return new BaseResponse<>(result);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 회원정보 수정 API
     */
    @PatchMapping("/{userIdx}")
    public BaseResponse<UserDto.UserRes> modifyUser(@PathVariable Long userIdx, @RequestBody @Valid UserDto.PatchReq patchReq) {
        try {
            Long userIdxByJwt = jwtService.getUserIdx();
            if (!userIdxByJwt.equals(userIdx)) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }
            UserDto.UserRes result = userService.updateUser(patchReq);
            return new BaseResponse<>(result);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 회원 탈퇴
     */
    @PatchMapping("/deletion/{userIdx}")
    public BaseResponse<UserDto.UserRes> deleteUser(@PathVariable Long userIdx) {
        try {
            Long userIdxByJwt = jwtService.getUserIdx();
            if (!userIdxByJwt.equals(userIdx)) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }
            UserDto.UserRes result = userService.deleteUser(userIdx);
            return new BaseResponse<>(result);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 닉네임 중복 여부 확인 API
     */
    @GetMapping("/exists")
    public BaseResponse<Boolean> checkNickname(@RequestParam String nickname) {
        boolean result = userService.existsByNickname(nickname);
        return new BaseResponse<>(result);
    }
}
