package com.mumulcom.mumulcom.src.user.domain;

import com.mumulcom.mumulcom.src.user.dto.UserProfileImg;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
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

    private String status;

    private LocalDateTime nicknameUpdatedAt;


    public void updateUserInfo(String nickname, String group, String profileImgUrl) {
        if (!this.nickname.equals(nickname)) {
            updateNicknameUpdatedAt();
        }
        this.nickname = nickname;
        this.group = group;
        this.profileImgUrl = profileImgUrl;
    }

    public void updateProfileImgUrlRandomly() {
        this.profileImgUrl = UserProfileImg.getRandomProfileImgUrl();
    }

    public void updateNicknameUpdatedAt() {
        this.nicknameUpdatedAt = LocalDateTime.now();
    }

    public void deleteUser() {
        this.nickname = "알 수 없음";
        this.profileImgUrl = UserProfileImg.getDeletedUserProfileImgUrl();
        this.status = "inactive";
    }
}
