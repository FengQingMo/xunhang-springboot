package com.xunhang.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("item")
public class Item {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;//物品ID

    private Long publisherId;//发布者id
    private String description;//描述
    private String location;//地点
    private LocalDate date;//日期
    private Boolean category;//0：失物，1:拾取的物品
    private Long claimerId;//认领者ID 0代表还没认领
    private String tag;
    private String title;
    private Boolean isDelete;
    private Boolean isSuccess;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
