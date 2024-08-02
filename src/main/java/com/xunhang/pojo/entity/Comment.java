package com.xunhang.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@ApiModel(value="com-xunhang-pojo-entity-Comment")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`comment`")
public class Comment {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="")
    private Long id;

    @TableField(value = "publisher_id")
    @ApiModelProperty(value="")
    private Long publisherId;

    @TableField(value = "content")
    @ApiModelProperty(value="")
    private String content;

    @TableField(value = "time")
    @ApiModelProperty(value="")
    private LocalDateTime time;

    @TableField(value = "item_id")
    @ApiModelProperty(value="")
    private Long itemId;

    @TableField(value = "reply_num")
    private Long replyNum;
    public static final String COL_ID = "id";

    public static final String COL_PUBLISHER_ID = "publisher_id";

    public static final String COL_CONTENT = "content";

    public static final String COL_TIME = "time";

    public static final String COL_ITEM_ID = "item_id";
}