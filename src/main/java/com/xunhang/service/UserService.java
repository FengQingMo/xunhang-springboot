package com.xunhang.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xunhang.common.result.PageResult;
import com.xunhang.pojo.dto.*;
import com.xunhang.pojo.entity.User;
import com.xunhang.pojo.vo.CommentVO;
import com.xunhang.pojo.vo.ItemVO;

import java.util.List;
import java.util.Map;

/**
 * @author 22477
 */

public interface UserService extends IService<User> {

    User getUser(String openid);

    User wxLogin(String openid);

    Boolean updateInfo(User user);

    String getOpenid(String code);

    Map<String,Integer> getBaseData();

    List<ItemVO> getMyItem(Boolean category);


    Boolean publish(ItemPublishDTO itemPublishDTO);


    PageResult getHomeItem(ItemHomeDTO itemHomeDTO);

    void saveComment(CommentDTO commentDTO);

    List<CommentVO> getComments(String id);

    Boolean replyComment(ReplyDTO replyDTO);

    void changeItemInfo(ItemChangeDTO itemChangeDTO);

    void deleteItem(Long id, Boolean category);
}
