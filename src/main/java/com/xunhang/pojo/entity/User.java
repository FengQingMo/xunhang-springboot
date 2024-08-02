package com.xunhang.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("user")
public class User {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private String openid;//openid
    private String nickname;//昵称
    private String phone;//联系方式 qq/phone//wexin
    private String password;//密码

    private String avatarUrl;//头像url

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
