package com.xunhang.pojo.entity;

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

@ApiModel(value="com-xunhang-pojo-entity-Message")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "message")
public class Message {
    @TableId(value = "id")
    @ApiModelProperty(value="")
    private Long id;

    @TableField(value = "content")
    @ApiModelProperty(value="")
    private String content;

    @TableField(value = "from_user_id")
    @ApiModelProperty(value="")
    private Long fromUserId;

    @TableField(value = "send_time")
    @ApiModelProperty(value="")
    private LocalDateTime sendTime;

    @TableField(value = "type")
    @ApiModelProperty(value="")
    private String type;

    @TableField(value = "to_user_id")
    @ApiModelProperty(value="")
    private Long toUserId;

    @TableField(value = "is_read")
    @ApiModelProperty(value="")
    private Boolean isRead;

    @TableField(value = "is_delete")
    @ApiModelProperty(value="")
    private Boolean isDelete;

    public static final String COL_ID = "id";

    public static final String COL_CONTENT = "content";

    public static final String COL_FROM_USER_ID = "from_user_id";

    public static final String COL_SEND_TIME = "send_time";

    public static final String COL_TYPE = "type";

    public static final String COL_TO_USER_ID = "to_user_id";

    public static final String COL_IS_READ = "is_read";

    public static final String COL_IS_DELETE = "is_delete";
}