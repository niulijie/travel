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
@Setter
@Getter
public class VideoCommentUserInfo {

    /**
     * 主键
     */
    private Integer commentId;

    /**
     * 短视频ID
     */
    private Integer videoId;

    private String userName;

    /**
     * 被评论ID
     */
    private Integer cid;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论者ID
     */
    private Integer commentator;

    /**
     * 添加时间
     */
    private LocalDateTime createTime;

}
