package com.mumulcom.mumulcom.src.user.controller;

import com.mumulcom.mumulcom.config.BaseResponse;
import com.mumulcom.mumulcom.src.user.domain.User;
import com.mumulcom.mumulcom.src.user.dto.UserResponseData;
import com.mumulcom.mumulcom.src.user.dto.UserSignUpData;
import com.mumulcom.mumulcom.src.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public BaseResponse<UserResponseData> createUser(@RequestBody @Valid UserSignUpData userSignUpData) {
        User user = userService.join(userSignUpData);
        UserResponseData userResponseData = getUserResponseData(user);
        return new BaseResponse<>(userResponseData);
    }

    private UserResponseData getUserResponseData(User user) {
        //TODO: user null 처리

        return UserResponseData.builder()
                .userIdx(user.getUserIdx())
                .build();
    }
}
