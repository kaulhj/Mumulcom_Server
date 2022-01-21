package com.mumulcom.mumulcom.src.user.domain;

import com.mumulcom.mumulcom.src.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "User")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;
    private String email;
    private String name;
    private String birth;
    private String ninkname;

    @Column(columnDefinition = "varchar(255) default 'Active'")
    private String status;

}
