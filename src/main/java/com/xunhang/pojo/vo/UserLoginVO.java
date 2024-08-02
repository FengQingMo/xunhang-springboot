package com.xunhang.pojo.vo;

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
    private String avatarUrl;
    private String nickname;
    private String createTime;
}
