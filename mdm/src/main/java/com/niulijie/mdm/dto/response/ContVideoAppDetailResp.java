package com.niulijie.mdm.dto.response;

import lombok.Data;

/**
 * <p>
 * 客户端视频详情返回值
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Data
public class ContVideoAppDetailResp implements IBatchSetUser, IBatchSetCollectStatus, IBatchSetLikeStatus {

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

    /**
     * 发布者
     */
    private Integer publisher;

    /**
     * 用户姓名
     */
    //private String name;

    /**
     * 头像
     */
    //private String avatar;

    /**
     * 部门id
     */
    //private Integer deptId;

    /**
     * 部门全路径
     */
    //private String deptPath;

    /**
     * 用户信息
     */
    private UserDetailResp userDetailResp;

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
     * 是否可被评论  1 默认 可以 2 不可
     */
    private Integer commented;

    /**
     * 是否可见评论  1 默认 可以  2 不可
     */
    private Integer visualed;

    /**
     * 喜欢状态
     */
    private Boolean likeStatus = false;

    /**
     * 收藏状态
     */
    private Boolean collectStatus = false;

    /**
     * 喜欢ID
     */
    private Integer likeId;

    /**
     * 收藏ID
     */
    private Integer collectId;

    /**
     * 删除标识 1正常 2 删除
     */
    private Integer deleted;

    /**
     * 获取用户id
     *
     * @return
     */
    @Override
    public Integer batchGetUserId() {
        return publisher;
    }

    /**
     * 设置用户信息的方法
     *
     * @param userDetailResp
     */
    @Override
    public void batchSetUser(UserDetailResp userDetailResp) {
//        this.name = userDetailResp.getName();
//        this.avatar = userDetailResp.getAvatar();
//        this.deptId = userDetailResp.getDeptId();
//        this.deptPath = userDetailResp.getDeptPath();
    }

    /**
     * 获取视频id
     *
     * @return
     */
    @Override
    public Integer batchGetVideoId() {
        return videoId;
    }

    /**
     * 设置用户喜欢状态的方法
     *
     * @param likeFlag
     */
    @Override
    public void batchSetLikeStatus(Boolean likeFlag) {
        this.likeStatus = likeFlag;
    }

    /**
     * 设置用户收藏状态
     *
     * @param collectFlag
     */
    @Override
    public void batchSetCollectStatus(Boolean collectFlag) {
        this.collectStatus = collectFlag;
    }
}
