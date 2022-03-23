package com.mumulcom.mumulcom.src.announce.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AnnounceRes {
    private long id;
    private String title;
    private String content;
    private String createdAt;
}
