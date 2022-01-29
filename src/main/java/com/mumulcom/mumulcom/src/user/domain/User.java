package com.mumulcom.mumulcom.src.user.domain;

import com.mumulcom.mumulcom.src.user.dto.UserDto;
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

    @Column(columnDefinition = "varchar(255) default 'Active'")
    private String status;

    public void updateUserInfo(UserDto.PatchReq patchReq) {
        this.nickname = patchReq.getNickname();
    }

    public void deleteUser() {
        this.status = "Inactive";
    }
}
