package com.mumulcom.mumulcom.src.reply.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdoptRes {
    private Long answerer;
    private Long questionIdx;
    private String noticeMessage;
}
