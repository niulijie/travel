package com.niulijie.mdm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 短视频表
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Data
@TableName("cont_video")
public class ContVideo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，视频id
     */
    @TableId(value = "video_id", type = IdType.AUTO)
    private Integer videoId;

    /**
     * 分类id
     */
    private Integer cateId;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 视频的云端地址
     */
    private String videoUrl;

    /**
     * 视频md5
     */
    private String videoMd5;

    /**
     * 图片的云端地址
     */
    private String pictureUrl;

    /**
     * 发布者
     */
    private Integer publisher;

    /**
     * 视频所属部门id
     */
    private Integer deptId;

    /**
     * 审核状态 0-待审核、1-通过、2-驳回
     */
    private Integer auditStatus;

    /**
     * 是否可被评论  1 默认 可以 2 不可
     */
    private Integer commented;

    /**
     * 是否可见评论  1 默认 可以  2 不可
     */
    private Integer visualed;

    /**
     * 喜欢数
     */
    private Integer likeCount;

    /**
     * 收藏数
     */
    private Integer collectCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 总数量
     */
    private Integer totalCount;

    /**
     * 浏览数
     */
    private Integer skimCount;

    /**
     * 浏览时长  
     */
    private Long skimTime;

    /**
     * 置顶状态  默认1 不置顶 2置顶
     */
    private Integer topStatus;

    /**
     * 置顶结束时间
     */
    private LocalDate topTime;

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
