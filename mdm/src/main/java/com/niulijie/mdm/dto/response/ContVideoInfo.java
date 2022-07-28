package com.niulijie.mdm.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 短视频表
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Setter
@Getter
public class ContVideoInfo {

    /**
     * 主键，视频id
     */
    private Integer videoId;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 视频的云端地址
     */
    private String videoUrl;

    /**
     * 图片的云端地址
     */
    private String pictureUrl;

    private Integer publisher;

    /**
     * 发布者
     */
    private String userName;

    private String avatar;

    /**
     * 视频所属部门id
     */
    private String deptPath;

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
     * 添加时间
     */
    private LocalDateTime createTime;

}
