package com.xunhang.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendVO {
    private String lastContent;
    private LocalDateTime lastContentTime;
    private Integer unreadNum;
    private UserVO friendInfo;
}
