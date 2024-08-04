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

    List<ReplyVO> replyVOList;

    private Long id;

    private Long parentId;

    private Long itemId;

    private String content;

    private UserVO userVO;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer likeCount;
}
