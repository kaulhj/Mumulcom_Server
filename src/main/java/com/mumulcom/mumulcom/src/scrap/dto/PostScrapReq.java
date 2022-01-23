package com.mumulcom.mumulcom.src.scrap.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostScrapReq {
    private long questionIdx;
    private long userIdx;
}
