package com.xunhang.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginVO {
    private String openid;
    private Long id;
    private String token;
    private String headImage;
    private String  username;
    private String headImageThumb;
    private String nickname;
    private String createTime;
    @ApiModelProperty(value = "每次请求都必须在header中携带accessToken")
    private String accessToken;

    @ApiModelProperty(value = "accessToken过期时间(秒)")
    private Integer accessTokenExpiresIn;

    @ApiModelProperty(value = "accessToken过期后，通过refreshToken换取新的token")
    private String refreshToken;

    @ApiModelProperty(value = "refreshToken过期时间(秒)")
    private Integer refreshTokenExpiresIn;
}
