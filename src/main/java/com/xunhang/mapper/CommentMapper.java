package com.xunhang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xunhang.pojo.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}