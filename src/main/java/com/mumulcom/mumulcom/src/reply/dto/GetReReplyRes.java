package com.mumulcom.mumulcom.src.reply.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetReReplyRes {
    private Long userIdx;
    private Long reReplyIdx;
    private String content;
    private String imageUrl;
    private String nickname;
    private String createdAt;
    private String profileImgUrl;
}
