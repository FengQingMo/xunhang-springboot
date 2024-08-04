package com.xunhang.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author 风清默
 * @date 2024/8/4 10:35
 * @description: 后台评论展示
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentAdminVO {
    private Long id;

    private Long parentId;

    private String headImage;

    private Long itemTitle;

    private String content;

    private String nickname;

    private String replyNickname;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer likeCount;
}
