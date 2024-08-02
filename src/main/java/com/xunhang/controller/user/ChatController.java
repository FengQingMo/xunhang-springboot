package com.xunhang.controller.user;


import com.xunhang.common.result.Result;
import com.xunhang.pojo.entity.Message;
import com.xunhang.pojo.vo.FriendVO;
import com.xunhang.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息页面
 */
@Slf4j
@RequestMapping("/chat")
@RestController
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/getFriends")
    public Result getFriends(){
        List<FriendVO>friends = chatService.getAllFriend();
        return Result.success(friends);
    }


    @GetMapping("/getMessages/{friendId}")
    public Result getMessages(@PathVariable("friendId") Long friendId){
        List<Message>messages = chatService.getMessages(friendId);
        return Result.success(messages);
    }

    @PostMapping("/refreshUnread/{friendId}")
    public Result refreshUnread(@PathVariable("friendId") Long friendId){
        chatService.refreshUnread(friendId);
        return Result.success("更新未读数量完成");
    }

    @PostMapping("/getOfflineMessage/{id}")
    public Result getOfflineMessage(@PathVariable("id")Long id){
       List<Message>messages= chatService.getOfflineMessage(id);
       return Result.success(messages);
    }
}
