package com.niulijie.mdm.dto.request;

import com.niulijie.mdm.result.PageBase;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Getter
@Setter
public class ContVideoCommentParam extends PageBase implements Serializable {

    private static final long serialVersionUID = 1L;

    public interface AddComment {}

    public interface ReplyComment {}

    public interface LikeComment {}

    public interface CommentList {}

    public interface CommentInfo {}

    public interface ReplyList {}

    public interface DeleteComment {}

    /**
     * 评论id
     */
    @NotNull(message = "评论id不能为空",groups = {LikeComment.class,ReplyList.class,DeleteComment.class,CommentInfo.class})
    private Integer commentId;

    /**
     * 短视频ID
     */
    @NotNull(message = "短视频id不能为空",groups = {AddComment.class,CommentList.class,ReplyComment.class})
    private Integer videoId;

    /**
     * 当前登录用户id
     */
    @NotNull(message = "当前登录用户id不能为空",groups = {CommentList.class,ReplyList.class})
    private Integer userId;

    /**
     * 视频发布者id
     */
    @NotNull(message = "视频发布者id",groups = {AddComment.class})
    private Integer publisher;

    /**
     * 被评论ID
     */
    @NotNull(message = "被评论id不能为空",groups = {ReplyComment.class})
    private Integer cid;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空",groups = {AddComment.class,ReplyComment.class})
    private String content;

    /**
     * 评论者ID
     */
    @NotNull(message = "评论者id不能为空",groups = {AddComment.class,LikeComment.class,ReplyComment.class,DeleteComment.class})
    private Integer commentator;

    /**
     * 回复人员
     */
    private Integer replier;

}
