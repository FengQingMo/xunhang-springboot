package com.xunhang.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunhang.mapper.CommentMapper;
import com.xunhang.pojo.entity.Comment;
import com.xunhang.service.CommentService;
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService{

}
