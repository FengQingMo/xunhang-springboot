package com.xunhang.pojo.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    private Long id;
    private String phone;
    private String nickname;
    private String avatarUrl;
}
