package com.xunhang.service;

import com.xunhang.pojo.vo.OnlineTerminalVO;

import java.util.List;

/**
 * @Author 风清默
 * @date 2024/8/3 14:11
 * @description: 即时通讯 接口层
 */
public interface ImService {

    /**
     * 判断是否在线
     * @param userIds 用户id集合，用 逗号分隔
     * @return
     */
    List<OnlineTerminalVO> getOnlineTerminals(String userIds);
}
