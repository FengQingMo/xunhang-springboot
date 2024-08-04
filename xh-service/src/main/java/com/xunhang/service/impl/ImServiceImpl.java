package com.xunhang.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.xunhang.common.constant.IMRedisKey;
import com.xunhang.common.enums.IMTerminalType;
import com.xunhang.pojo.vo.OnlineTerminalVO;
import com.xunhang.service.ImService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 风清默
 * @date 2024/8/3 14:11
 * @description: 即时通讯实现类
 */
@RequiredArgsConstructor
@Service
public class ImServiceImpl implements ImService {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public List<OnlineTerminalVO> getOnlineTerminals(String userIds) {
        List<Long> userIdList = Arrays.stream(userIds.split(",")).map(Long::parseLong).collect(Collectors.toList());
        List<OnlineTerminalVO> vos = new ArrayList<>();
        for (Long id : userIdList) {
            List<Integer> terminals = new ArrayList<>();
            for (Integer terminal : IMTerminalType.codes()) {
                String key = IMRedisKey.getUserServerKey(id, terminal);
                String value = stringRedisTemplate.opsForValue().get(key);
                if (value != null) {
                    terminals.add(terminal);
                }
            }
            if (CollectionUtil.isNotEmpty(terminals)) {
                vos.add(new OnlineTerminalVO(id, terminals));
            }
        }
        return vos;
    }
}
