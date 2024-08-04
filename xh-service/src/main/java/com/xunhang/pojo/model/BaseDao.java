package com.xunhang.pojo.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author 风清默
 * @date 2024/7/5 20:50
 * @Package com.xunhang.pojo.model
 * @description: 数据库实体基类
 */
@Data
public class BaseDao implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 删除标记
     */
    @TableField(fill = FieldFill.INSERT)
    private String delFlag;
}
