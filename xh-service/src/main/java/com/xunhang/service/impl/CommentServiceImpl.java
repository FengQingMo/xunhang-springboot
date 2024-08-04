package com.xunhang.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunhang.common.utils.UserUtil;
import com.xunhang.mapper.CommentMapper;
import com.xunhang.pojo.dto.CommentDTO;
import com.xunhang.pojo.dto.ReplyDTO;
import com.xunhang.pojo.entity.Comment;
import com.xunhang.pojo.entity.Reply;
import com.xunhang.pojo.vo.CommentAdminVO;
import com.xunhang.pojo.vo.CommentVO;
import com.xunhang.pojo.vo.ConditionVO;
import com.xunhang.pojo.vo.ReplyVO;
import com.xunhang.service.CommentService;
import com.xunhang.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService{

    private final ReplyService replyService;
    private final CommentMapper commentMapper;

    @Override
    public void saveComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        BeanUtil.copyProperties(commentDTO, comment);
        comment.setUserId(UserUtil.getCurrentId());
        save(comment);
    }

    @Override
    public List<CommentVO> getCommentsByItemId(Long id) {
        //todo 连表查询物品下的评论
        List<CommentVO> commentVOList = commentMapper.getCommentsByItemId(id);
        if (commentVOList == null || commentVOList.isEmpty())
            return Collections.emptyList();
        List<Long> commentIds = commentVOList.stream()
                .map(CommentVO::getId)
                .collect(Collectors.toList());
        List<ReplyVO> replyVOList = commentMapper.getRepliesByCommentIds(commentIds);
        Map<Long, List<ReplyVO>> replyMap = replyVOList.stream()
                .collect(Collectors.groupingBy(ReplyVO::getParentId));
        commentVOList.forEach(item -> item.setReplyVOList(replyMap.get(item.getId())));
        return commentVOList;
    }
    @Override
    public Boolean replyComment(ReplyDTO replyDTO) {
        Reply reply = new Reply();
        BeanUtil.copyProperties(replyDTO, reply);
        reply.setTime(LocalDateTime.now());
        reply.setPublisherId(UserUtil.getCurrentId());
         replyService.save(reply);
        return  true;
    }

    @Override
    public List<ReplyVO> getRepliesByCommentId(Long commentId) {
        return commentMapper.getRepliesByCommentIds(Collections.singletonList(commentId));
    }

    @SneakyThrows
    @Override
    public IPage<CommentAdminVO> listCommentsAdmin(ConditionVO conditionVO) {
        return  null;
    }
}
