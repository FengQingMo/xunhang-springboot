package com.xunhang.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyVO {

    private Long id;

    private Long parentId;

    private Long userId;

    private String nickname;

    private String headImage;

    private Integer replyUserId;

    private String replyNickname;

    private String content;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
