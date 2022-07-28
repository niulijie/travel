package com.niulijie.mdm.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 客户端视频列表返回值
 * </p>
 * @author
 * @since 2022-06-08
 */
@Data
public class ContVideoAppListResp implements IBatchSetCollectStatus, IBatchSetLikeStatus {

    /**
     * 主键，视频id
     */
    private Integer videoId;

    /**
     * 发布者
     */
    private Integer publisher;

    /**
     * 视频发布者姓名
     */
    private String userName;

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

    /**
     * 喜欢数
     */
    private Integer likeCount;

    /**
     * 收藏数
     */
    private Integer collectCount;

    /**
     * 喜欢状态
     */
    private Boolean likeStatus = false;

    /**
     * 收藏状态
     */
    private Boolean collectStatus = false;

    /**
     * 添加时间
     */
    private LocalDateTime createTime;

    /**
     * 获取视频id
     * @return
     */
    @Override
    public Integer batchGetVideoId() {
        return videoId;
    }

    /**
     * 设置用户收藏状态
     * @param collectFlag
     */
    @Override
    public void batchSetCollectStatus(Boolean collectFlag) {
        this.collectStatus = collectFlag;
    }

    /**
     * 设置用户喜欢状态的方法
     * @param likeFlag
     */
    @Override
    public void batchSetLikeStatus(Boolean likeFlag) {
        this.likeStatus = likeFlag;
    }
}
