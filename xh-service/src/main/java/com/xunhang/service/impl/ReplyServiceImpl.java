package com.xunhang.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunhang.pojo.entity.Reply;
import com.xunhang.mapper.ReplyMapper;
import com.xunhang.service.ReplyService;
@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements ReplyService{

}
