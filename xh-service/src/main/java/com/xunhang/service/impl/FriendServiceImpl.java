package com.xunhang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunhang.common.constant.RedisKey;
import com.xunhang.common.enums.ExceptionEnums;
import com.xunhang.common.exception.GlobalException;
import com.xunhang.common.utils.UserUtil;
import com.xunhang.mapper.FriendMapper;
import com.xunhang.mapper.UserMapper;
import com.xunhang.pojo.entity.Friend;
import com.xunhang.pojo.entity.User;
import com.xunhang.pojo.vo.FriendVO;
import com.xunhang.service.FriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = RedisKey.IM_CACHE_FRIEND)
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService {

    private final UserMapper userMapper;

    @Override
    public List<Friend> findFriendByUserId(Long userId) {
        return query().eq("user_id", userId).list();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addFriend(Long friendId) {
        long userId = UserUtil.getCurrentId();
        if (userId == friendId) {
            throw new GlobalException(ExceptionEnums.ADDSELF_NOT_PERMIT);
        }
        // 互相绑定好友关系
        FriendServiceImpl proxy = (FriendServiceImpl) AopContext.currentProxy();
        proxy.bindFriend(userId, friendId);
        proxy.bindFriend(friendId, userId);
        log.info("添加好友，用户id:{},好友id:{}", userId, friendId);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delFriend(Long friendId) {
        long userId = UserUtil.getCurrentId();
        // 互相解除好友关系，走代理清理缓存
        FriendServiceImpl proxy = (FriendServiceImpl) AopContext.currentProxy();
        proxy.unbindFriend(userId, friendId);
        proxy.unbindFriend(friendId, userId);
        log.info("删除好友，用户id:{},好友id:{}", userId, friendId);
    }


    @Cacheable(key = "#userId1+':'+#userId2")
    @Override
    public Boolean isFriend(Long userId1, Long userId2) {
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Friend::getUserId, userId1)
                .eq(Friend::getFriendId, userId2);
        return query().eq("user_id",userId1).eq("friend_id",userId2).count() > 0;
    }


    @Override
    public void update(FriendVO vo) {
        long userId = UserUtil.getCurrentId();
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Friend::getUserId, userId)
                .eq(Friend::getFriendId, vo.getId());

        Friend f = this.getOne(queryWrapper);
        if (f == null) {
            throw new GlobalException(ExceptionEnums.NOT_YOUR_FRIEND);
        }

        f.setFriendHeadImage(vo.getHeadImage());
        f.setFriendNickname(vo.getNickname());
        this.updateById(f);
    }


    /**
     * 单向绑定好友关系
     *
     * @param userId   用户id
     * @param friendId 好友的用户id
     */
    @CacheEvict(key = "#userId+':'+#friendId")
    public void bindFriend(Long userId, Long friendId) {
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Friend::getUserId, userId)
                .eq(Friend::getFriendId, friendId);
        if (query().eq("user_id",userId).eq("friend_id",friendId).count() == 0) {
            Friend friend = new Friend();
            friend.setUserId(userId);
            friend.setFriendId(friendId);
            User friendInfo = userMapper.selectById(friendId);
            friend.setFriendHeadImage(friendInfo.getHeadImage());
            friend.setFriendNickname(friendInfo.getNickname());
            this.save(friend);
        }
    }


    /**
     * 单向解除好友关系
     *
     * @param userId   用户id
     * @param friendId 好友的用户id
     */
    @CacheEvict(key = "#userId+':'+#friendId")
    public void unbindFriend(Long userId, Long friendId) {
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Friend::getUserId, userId)
                .eq(Friend::getFriendId, friendId);
        List<Friend> friends = this.list(queryWrapper);
        friends.forEach(friend -> this.removeById(friend.getId()));
    }


    @Override
    public FriendVO findFriend(Long friendId) {

        QueryWrapper<Friend> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(Friend::getUserId, UserUtil.getCurrentId())
                .eq(Friend::getFriendId, friendId);
        Friend friend = this.getOne(wrapper);
        if (friend == null) {
            throw new GlobalException(ExceptionEnums.NOT_YOUR_FRIEND);
        }
        FriendVO vo = new FriendVO();
        vo.setId(friend.getFriendId());
        vo.setHeadImage(friend.getFriendHeadImage());
        vo.setNickname(friend.getFriendNickname());
        return vo;
    }
}
