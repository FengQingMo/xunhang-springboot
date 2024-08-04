package com.xunhang.session;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserSession extends IMSessionInfo {

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    public UserSession(Long userId, Integer terminal) {
        super(userId, terminal);
    }
}
