package com.niulijie.mdm.controller;

import com.niulijie.mdm.dto.request.ContVideoCommentParam;
import com.niulijie.mdm.dto.response.ContVideoCommentInfo;
import com.niulijie.mdm.result.BaseResult;
import com.niulijie.mdm.result.ResultUtil;
import com.niulijie.mdm.service.AppVideoCommentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * app评论
 * @author df
 */
@RestController
@RequestMapping("/app/")
public class AppVideoCommentController {

    private final AppVideoCommentService commentCollectionService;

    public AppVideoCommentController(AppVideoCommentService commentCollectionService) {
        this.commentCollectionService = commentCollectionService;
    }

    /**
     * 添加评论
     */
    @PostMapping("comment/add")
    public BaseResult addComment(@Validated(ContVideoCommentParam.AddComment.class)
                                     @RequestBody ContVideoCommentParam contVideoCommentParam) {
        Integer integer = commentCollectionService.addComment(contVideoCommentParam);
        if (integer > 0) {
            return ResultUtil.ok(integer);
        }
        return ResultUtil.error();
    }

    /**
     * 评论回复
     */
    @PostMapping("comment/reply")
    public BaseResult replyComment(@Validated(ContVideoCommentParam.ReplyComment.class)
                                 @RequestBody ContVideoCommentParam contVideoCommentParam) {
        Integer integer = commentCollectionService.replyComment(contVideoCommentParam);
        if (integer > 0) {
            return ResultUtil.ok(integer);
        }
        return ResultUtil.error();
    }

    /**
     * 根据评论id查询评论信息
     */
    @PostMapping("comment/info")
    public BaseResult commentInfo(@Validated(ContVideoCommentParam.CommentInfo.class)
                                   @RequestBody ContVideoCommentParam contVideoCommentParam) {
        ContVideoCommentInfo contVideoComment = commentCollectionService.commentInfo(contVideoCommentParam);
        return ResultUtil.ok(contVideoComment);
    }

    /**
     * 评论点赞
     */
    @PostMapping("comment/like")
    public BaseResult likeComment(@Validated(ContVideoCommentParam.LikeComment.class)
                                      @RequestBody ContVideoCommentParam contVideoCommentParam) {
        Integer integer = commentCollectionService.likeComment(contVideoCommentParam);
        if (integer > 0) {
            return ResultUtil.ok();
        }
        return ResultUtil.error();
    }

    /**
     * 评论点赞取消
     */
    @PostMapping("comment/like/cancel")
    public BaseResult likeCommentCancel(@Validated(ContVideoCommentParam.LikeComment.class)
                                  @RequestBody ContVideoCommentParam contVideoCommentParam) {
        Integer integer = commentCollectionService.likeCommentCancel(contVideoCommentParam);
        if (integer > 0) {
            return ResultUtil.ok();
        }
        return ResultUtil.error();
    }

    /**
     * 评论删除
     */
    @PostMapping("comment/delete")
    public BaseResult deleteComment(@Validated(ContVideoCommentParam.DeleteComment.class)
                                  @RequestBody ContVideoCommentParam contVideoCommentParam) {
        Integer integer = commentCollectionService.deleteComment(contVideoCommentParam);
        if (integer > 0) {
            return ResultUtil.ok();
        }
        return ResultUtil.error();
    }

    /**
     * 评论列表
     */
    @PostMapping("comment/list")
    public BaseResult commentList(@Validated(ContVideoCommentParam.CommentList.class)
                                      @RequestBody ContVideoCommentParam contVideoCommentParam) {
        Map<String, Object> map =  commentCollectionService.commentList(contVideoCommentParam);
        return ResultUtil.ok(map);
    }

    /**
     * 评论回复列表
     */
    @PostMapping("comment/reply/list")
    public BaseResult commentReplyList(@Validated(ContVideoCommentParam.ReplyList.class)
                                           @RequestBody ContVideoCommentParam contVideoCommentParam) {
        Map<String, Object> map = commentCollectionService.commentReplyList(contVideoCommentParam);
        return ResultUtil.ok(map);
    }

}
