package com.niulijie.mdm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Data
@TableName("cont_video_comment")
public class ContVideoComment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Integer commentId;

    /**
     * 短视频ID
     */
    private Integer videoId;

    /**
     * 视频发布者id
     */
    private Integer publisher;

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
     * 回复人员
     */
    private Integer replier;

    /**
     * 获赞数
     */
    private Integer likeCount;

    /**
     * 评论路径
     */
    private String commentPath;

    /**
     * 评论对象：0-短视频、1-评论
     */
    private Integer commentType;

    /**
     * 删除标识 1正常 2 删除
     */
    private Integer deleted;

    /**
     * 添加时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
