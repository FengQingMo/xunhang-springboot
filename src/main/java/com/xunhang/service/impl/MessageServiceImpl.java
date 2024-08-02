package com.xunhang.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunhang.pojo.entity.Message;
import com.xunhang.mapper.MessageMapper;
import com.xunhang.service.MessageService;
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService{

}
