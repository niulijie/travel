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
public class ContVideoCommentInfo {

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
     * 评论回复数量
     */
    private Integer replyCount = 0;

    private String content;

    /**
     * 点赞数量
     */
    private Integer likeCount;

    /**
     * 时间
     */
    private LocalDateTime createTime;

}
