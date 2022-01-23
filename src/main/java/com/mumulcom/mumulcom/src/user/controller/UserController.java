package com.mumulcom.mumulcom.src.user.controller;

import com.mumulcom.mumulcom.config.BaseException;
import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.src.user.dto.UserDto.SignInReq;
import com.mumulcom.mumulcom.src.user.dto.UserDto.SignInRes;
import com.mumulcom.mumulcom.src.user.dto.UserDto.SignUpReq;
import com.mumulcom.mumulcom.src.user.dto.UserDto.SignUpRes;
import com.mumulcom.mumulcom.src.user.service.UserService;
import com.mumulcom.mumulcom.utils.JwtService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
