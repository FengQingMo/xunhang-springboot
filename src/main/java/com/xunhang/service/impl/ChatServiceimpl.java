package com.xunhang.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.xunhang.common.UserHolder;
import com.xunhang.constant.RedisConstants;
import com.xunhang.mapper.FriendMapper;
import com.xunhang.pojo.entity.Friend;
import com.xunhang.pojo.entity.Message;
import com.xunhang.pojo.entity.User;
import com.xunhang.pojo.vo.FriendVO;
import com.xunhang.pojo.vo.UserVO;
import com.xunhang.service.ChatService;
import com.xunhang.service.FriendService;
import com.xunhang.service.MessageService;
import com.xunhang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class ChatServiceimpl implements ChatService {

    @Autowired
    private FriendService friendService;
    @Autowired
    private MessageService messageService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private UserService userService;
    Lock lock = new ReentrantLock();

    @Override
    public List<FriendVO> getAllFriend() {
        Long id = UserHolder.getCurrentId();
        List<Friend> friends = friendService.query().eq("user_id", id).list();
        List<FriendVO> friendVOS = new ArrayList<>();

        friends.forEach((friend -> {
            Long friendId = friend.getFriendId();
            User user = userService.query().eq("id", friendId).one();
            UserVO userVO = new UserVO();
            BeanUtil.copyProperties(user, userVO);
            FriendVO friendVO = new FriendVO();
            BeanUtil.copyProperties(friend,friendVO);
            friendVO.setFriendInfo(userVO);
            friendVOS.add(friendVO);
        }));

        return friendVOS;
    }


    @Override
    public void saveMessage(Message message) {

        message.setSendTime(LocalDateTime.now());
        message.setIsDelete(false);
        messageService.save(message);
    }

    /**
     * 判断是否是第一次聊天，是则新增朋友关系，不是则更新最后一条消息
     *
     * @param fromUser
     * @param toUser
     * @param content
     */
    @Transactional
    @Override
    public void isFirstChat(Long fromUser, Long toUser, String content) {
        Integer count = friendService.query().eq("user_id", fromUser).eq("friend_id", toUser).count();
        count += friendService.query().eq("friend_id", fromUser).eq("user_id", toUser).count();

        if (count == 0) {
            Friend friend = new Friend();
            friend.setFriendId(toUser);
            friend.setUserId(fromUser);
            friend.setLastContentTime(LocalDateTime.now());
            friend.setLastContent(content);
            friendService.save(friend);


            Friend friend1 = new Friend();
            friend1.setFriendId(fromUser);
            friend1.setUserId(toUser);
            friend1.setLastContentTime(LocalDateTime.now());
            friend1.setLastContent(content);
            friendService.save(friend1);

        } else {
                Integer unreadNum = friendService.query().eq("user_id", toUser).eq("friend_id", fromUser).one().getUnreadNum();
              friendMapper.updateFriend(fromUser,toUser,content,LocalDateTime.now(),0);
            friendMapper.updateFriend(toUser,fromUser,content,LocalDateTime.now(),unreadNum+1);
        }
        Message message = new Message();
        message.setContent(content);
        message.setFromUserId(fromUser);
        message.setToUserId(toUser);
        saveMessage(message);

    }

    @Override
    public List<Message> getMessages(Long friendId) {
        Long id = UserHolder.getCurrentId();
        List<Message> list = messageService.query().eq("from_user_id", id).eq("to_user_id", friendId).list();
        List<Message> list1 = messageService.query().eq("from_user_id", friendId).eq("to_user_id", id).list();
        list.addAll(list1);
       list = list.stream().sorted(Comparator.comparing(Message::getSendTime)).collect(Collectors.toList());
        return list;

    }

    @Override
    public void refreshUnread(Long friendId) {
        Long id = UserHolder.getCurrentId();
        friendService.update().eq("user_id",id).eq("friend_id",friendId).set("unread_num",0).update();
    }

    @Override
    public List<Message> getOfflineMessage(Long id) {
        String key = RedisConstants.MESSAGE_QUEUE_KEY + id;
        Set<String> ranges = redisTemplate.opsForZSet().range(key, 0, -1);
        if (ranges != null) {
            List<Message> messages = ranges.stream().map(range -> JSONUtil.toBean(range, Message.class)).collect(Collectors.toList());
            return messages;
        }
        return new ArrayList<>(0);


    }
}
