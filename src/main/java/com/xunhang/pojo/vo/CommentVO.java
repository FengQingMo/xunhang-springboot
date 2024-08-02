package com.xunhang.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVO {
    private Long id;
    private String content;
    private UserVO userVO;
    private LocalDateTime time;
    private Long replyNum;
    List<ReplyVO> replyVOList;
}
