package com.niulijie.mdm.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
public class ContVideoCommentList {

    /**
     * 评论id
     */
    private Integer commentId;

    private Integer userId;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数量
     */
    private Integer likeCount;

    /**
     * 评论回复数量
     */
    private Integer replyCount = 0;

    /**
     * 当前登陆人员是否点赞此条评论，默认false
     */
    private Boolean likeStatus = false;

    /**
     * 时间
     */
    private LocalDateTime createTime;

}
