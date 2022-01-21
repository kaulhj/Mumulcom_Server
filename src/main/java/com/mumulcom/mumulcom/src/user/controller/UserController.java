package com.mumulcom.mumulcom.src.user.controller;

import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.src.user.domain.User;
import com.mumulcom.mumulcom.src.user.dto.UserJwtData;
import com.mumulcom.mumulcom.src.user.dto.UserResponseData;
import com.mumulcom.mumulcom.src.user.dto.UserSignInData;
import com.mumulcom.mumulcom.src.user.dto.UserSignUpData;
import com.mumulcom.mumulcom.src.user.service.UserService;
import com.mumulcom.mumulcom.utils.JwtService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

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
     *
     * @param userSignUpData
     * @return
     */
    @PostMapping
    public BaseResponse<UserResponseData> createUser(@RequestBody @Valid UserSignUpData userSignUpData) {
        User user = userService.join(userSignUpData);
        UserResponseData userResponseData = getUserResponseData(user);
        return new BaseResponse<>(userResponseData);
    }

    /**
     * 로그인 API
     *
     * @param userSignInData
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<UserJwtData> login(@RequestBody @Valid UserSignInData userSignInData) {
        Optional<UserJwtData> userJwtData = userService.login(userSignInData);
        if (userJwtData.isEmpty()) {
//            return new BaseResponse<>(LOGIN_FAIL);
        }
        return new BaseResponse<>(userJwtData.get());
       
    }


    private UserResponseData getUserResponseData(User user) {
        //TODO: user null 처리

        return UserResponseData.builder()
                .userIdx(user.getUserIdx())
                .build();
    }
}
