package com.mumulcom.mumulcom.src.user.domain;

import com.mumulcom.mumulcom.src.user.dto.UserProfileImg;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicInsert
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String nickname;

    @NotBlank
    private String group;

    private String profileImgUrl;

    @Column(columnDefinition = "varchar(255) default 'Active'")
    private String status;

    public void updateUserInfo(String nickname, String group, String profileImgUrl) {
        this.nickname = nickname;
        this.group = group;
        this.profileImgUrl = profileImgUrl;
    }

    public void updateProfileImgUrlRandomly() {
        this.profileImgUrl = UserProfileImg.getRandomProfileImgUrl();
    }

    public void deleteUser() {
        this.nickname = "알 수 없음";
        this.profileImgUrl = UserProfileImg.getDeletedUserProfileImgUrl();
        this.status = "Inactive";
    }
}
