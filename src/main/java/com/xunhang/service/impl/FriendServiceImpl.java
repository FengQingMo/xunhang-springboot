package com.xunhang.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunhang.mapper.FriendMapper;
import com.xunhang.pojo.entity.Friend;
import com.xunhang.service.FriendService;
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService{

}
