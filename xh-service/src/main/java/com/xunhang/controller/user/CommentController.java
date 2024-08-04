package com.xunhang.controller.user;

import com.xunhang.common.result.Result;
import com.xunhang.common.result.ResultUtils;
import com.xunhang.pojo.dto.CommentDTO;
import com.xunhang.pojo.dto.ReplyDTO;
import com.xunhang.pojo.vo.CommentVO;
import com.xunhang.pojo.vo.ConditionVO;
import com.xunhang.pojo.vo.ReplyVO;
import com.xunhang.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author 风清默
 * @date 2024/8/3 14:16
 * @description: 评论控制器层
 */
@Api("评论控制器层")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 获取评论
     */
    @GetMapping("/comment/{itemId}")
    @ApiOperation("获取物品评论")
    public Result getCommentsByItemId(@PathVariable("itemId") Long id) {
        List<CommentVO> commentVOS = commentService.getCommentsByItemId(id);
        return ResultUtils.success(commentVOS);
    }

    /**
     * 发布评论
     */
    @PostMapping("/publishComment")
    @ApiOperation("发布评论")
    public Result publishComment(@RequestBody CommentDTO commentDTO) {
        commentService.saveComment(commentDTO);
        return ResultUtils.success("发布评论成功");
    }

    /**
     * 回复评论
     */
    @PostMapping("/replyComment")
    @ApiOperation("回复评论")
    public Result replyComment(@RequestBody ReplyDTO replyDTO) {
        return ResultUtils.success(commentService.replyComment(replyDTO));
    }

    @ApiOperation(value = "根据commentId获取回复")
    @GetMapping("/comments/{commentId}/replies")
    public Result<List<ReplyVO>> getRepliesByCommentId(@PathVariable("commentId") Long commentId) {
        return ResultUtils.success(commentService.getRepliesByCommentId(commentId));
    }

    @ApiOperation(value = "查询后台评论")
    @GetMapping("/admin/comments")
    public Result listCommentBackDTO(ConditionVO conditionVO) {
        return ResultUtils.success(commentService.listCommentsAdmin(conditionVO));
    }
}
