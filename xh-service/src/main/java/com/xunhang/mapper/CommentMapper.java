package com.xunhang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xunhang.pojo.entity.Comment;
import com.xunhang.pojo.vo.CommentVO;
import com.xunhang.pojo.vo.ReplyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    List<CommentVO> getCommentsByItemId(@Param("id") Long id);

    List<ReplyVO> getRepliesByCommentIds(@Param("commentIds") List<Long> commentIdList);
}