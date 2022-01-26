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
            return new BaseResponse<>(userService.join(signUpReq));
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
            SignInRes signInRes = userService.login(signInReq);
            return new BaseResponse<>(signInRes);
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
            UserDto.UserRes user = userService.getUser(userIdx);
            return new BaseResponse<>(user);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 회원정보 수정 API
     */
    @PatchMapping("/{userIdx}")
    public BaseResponse<UserDto.UserRes> modifyUser(@PathVariable Long userIdx, @RequestBody UserDto.PatchReq patchReq) {
        try {
            Long userIdxByJwt = jwtService.getUserIdx();
            if (!userIdxByJwt.equals(userIdx)) {
                throw new BaseException(BaseResponseStatus.INVALID_JWT);
            }
            UserDto.UserRes user = userService.updateUser(patchReq);
            return new BaseResponse<>(user);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
