package com.xunhang.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User  {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 微信用户唯一id openid
     */
    private String openid;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 电话
     */
    private String phone;
    /**
     * 密码
     */
    private String password;
    /**
     * 最后登录时间
     */
    private Date lastLoginTime;
    /**
     * 用户类型  1:普通用户 2:审核专用账户
     */
    private Integer type;
    /**
     * 头像
     */
    private String headImage;
    /**
     * 头像缩略图
     */
    @TableField("head_image_thumb")
    private String headImageThumb;

       /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
        /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 删除标记
     */
    @TableField(fill = FieldFill.INSERT)

    private String isDel;
}
