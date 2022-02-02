package com.mumulcom.mumulcom.src.user.dto;

import lombok.Getter;

import java.util.Random;

@Getter
public enum UserProfileImg {
    CHARACTER_1("https://mumulcom-bucket.s3.ap-northeast-2.amazonaws.com/character/1.png"),
    CHARACTER_2("https://mumulcom-bucket.s3.ap-northeast-2.amazonaws.com/character/2.png"),
    CHARACTER_3("https://mumulcom-bucket.s3.ap-northeast-2.amazonaws.com/character/3.png"),
    CHARACTER_4("https://mumulcom-bucket.s3.ap-northeast-2.amazonaws.com/character/4.png"),
    CHARACTER_5("https://mumulcom-bucket.s3.ap-northeast-2.amazonaws.com/character/5.png"),
    CHARACTER_6("https://mumulcom-bucket.s3.ap-northeast-2.amazonaws.com/character/6.png"),

    ;

    private final String imgUrl;
    UserProfileImg(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public static String getRandomProfileImgUrl() {
        return UserProfileImg.values()[new Random().nextInt(UserProfileImg.values().length)].getImgUrl();
    }
}
