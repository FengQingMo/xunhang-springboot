package com.xunhang.service;


import com.xunhang.pojo.entity.Message;
import com.xunhang.pojo.vo.FriendVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatService {
    List<FriendVO> getAllFriend();

    void saveMessage(Message message);



    void isFirstChat(Long fromUser, Long toUser, String content);

    List<Message> getMessages(Long friendId);

    void refreshUnread(Long friendId);

    List<Message> getOfflineMessage(Long id);
}
