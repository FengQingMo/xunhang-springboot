package com.xunhang.pojo.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户信息VO")
public class UserVO {
    private Long id;
    private String username;
    private String nickname;
    private Integer type;
    private String phone;
    private String headImage;
    private String headImageThumb;
    private Boolean online;
}
