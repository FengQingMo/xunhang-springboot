package com.xunhang.session;

import lombok.Data;

@Data
public class IMSessionInfo {
    /**
     * 用户id
     */
    private Long userId;

    public IMSessionInfo() {
    }

    /**
     * 终端类型
     */
    private Integer terminal;

    public IMSessionInfo(Long userId, Integer terminal) {
        this.userId = userId;
        this.terminal = terminal;
    }
}
