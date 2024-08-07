package com.xunhang.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    @ApiModelProperty(name = "replyUserId", value = "回复用户id", dataType = "Integer")
    private Long replyUserId;

    @ApiModelProperty(name = "itemId", value = "物品id", dataType = "Integer")
    private Long itemId;

    @NotBlank(message = "评论内容不能为空")
    @ApiModelProperty(name = "content", value = "评论内容", required = true, dataType = "String")
    private String content;

    @ApiModelProperty(name = "parentId", value = "评论父id", dataType = "Integer")
    private Long parentId;
}
