package com.xunhang.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "item_image")
public class ItemImage {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="")
    private Long id;

    @TableField(value = "image_url")
    @ApiModelProperty(value="")
    private String imageUrl;

    @TableField(value = "item_id")
    @ApiModelProperty(value="")
    private Long itemId;

    public static final String COL_ID = "id";

    public static final String COL_IMAGE_URL = "image_url";

    public static final String COL_ITEM_ID = "item_id";

    public ItemImage(String imageUrl, Long itemId) {
        this.imageUrl = imageUrl;
        this.itemId = itemId;
    }
}