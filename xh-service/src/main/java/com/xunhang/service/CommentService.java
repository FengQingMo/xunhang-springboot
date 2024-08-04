package com.xunhang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xunhang.pojo.dto.CommentDTO;
import com.xunhang.pojo.dto.ReplyDTO;
import com.xunhang.pojo.entity.Comment;
import com.xunhang.pojo.vo.CommentAdminVO;
import com.xunhang.pojo.vo.CommentVO;
import com.xunhang.pojo.vo.ConditionVO;
import com.xunhang.pojo.vo.ReplyVO;

import java.util.List;

public interface CommentService extends IService<Comment> {

    void saveComment(CommentDTO commentDTO);

    List<CommentVO> getCommentsByItemId(Long id);

    Boolean replyComment(ReplyDTO replyDTO);

    List<ReplyVO> getRepliesByCommentId(Long commentId);

    IPage<CommentAdminVO> listCommentsAdmin(ConditionVO conditionVO);
}
