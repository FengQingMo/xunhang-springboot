package com.xunhang.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("comment")
public class Comment {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long userId;

    private Integer replyUserId;

    private Long itemId;

    private String content;

    private Integer parentId;


    private Integer isDelete;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
