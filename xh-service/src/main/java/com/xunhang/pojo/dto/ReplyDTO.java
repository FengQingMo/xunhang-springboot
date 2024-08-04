package com.xunhang.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
    private Long itemId;
    private String content;
    private Long publisherId;
    private Long commentId;
    private Long replyUserId;
    private String replyUsername;
}
