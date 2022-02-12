package com.mumulcom.mumulcom.src.user.domain;


import lombok.Getter;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@MappedSuperclass
public class BaseTimeEntity {
    protected ZonedDateTime createdAt;
    protected ZonedDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        this.updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}
