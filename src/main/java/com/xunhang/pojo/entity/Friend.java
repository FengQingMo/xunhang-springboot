package com.xunhang.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@ApiModel(value="com-xunhang-pojo-entity-Friend")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "friend")
public class Friend {
    @TableField(value = "user_id")
    @ApiModelProperty(value="")
    private Long userId;

    @TableField(value = "friend_id")
    @ApiModelProperty(value="")
    private Long friendId;

    @TableField(value = "last_content_type")
    @ApiModelProperty(value="")
    private String lastContentType;

    @TableField(value = "id")
    @ApiModelProperty(value="")
    private Long id;

    @TableField(value = "status")
    @ApiModelProperty(value="")
    private Integer status;

    @TableField(value = "last_content")
    @ApiModelProperty(value="")
    private String lastContent;

    @TableField(value = "last_content_time")
    private LocalDateTime lastContentTime;

    private Integer unreadNum;

    public static final String COL_USER_ID = "user_id";

    public static final String COL_FRIEND_ID = "friend_id";

    public static final String COL_LAST_CONTENT_TYPE = "last_content_type";

    public static final String COL_ID = "id";

    public static final String COL_STATUS = "status";

    public static final String COL_LAST_CONTENT = "last_content";
}