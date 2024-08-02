package com.xunhang.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyVO {
    private Long itemId;
    private String content;
    private Long commentId;
    private LocalDateTime time;
    private UserVO userVO;

}
