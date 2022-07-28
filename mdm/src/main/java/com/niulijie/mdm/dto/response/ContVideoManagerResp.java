package com.niulijie.mdm.dto.response;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 短视频-页面返回值
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Data
public class ContVideoManagerResp implements IBatchSetUser{

    /**
     * 主键，视频id
     */
    private Integer videoId;

    /**
     * 分类id
     */
    private Integer cateId;

    /**
     * 专栏名称
     */
    private String cateName;

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
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 部门id
     */
    private Integer deptId;

    /**
     * 部门全路径
     */
    private String deptPath;

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
     * 置顶状态  默认1 不置顶 2置顶
     */
    private Integer topStatus;

    /**
     * 置顶结束时间
     */
    private LocalDate topTime;

    /**
     * 添加时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

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
        this.deptId = userDetailResp.getDeptId();
        //this.deptName = userDetailResp.getDeptName();
        //this.deptLevel = userDetailResp.getDeptLevel();
        this.deptPath = userDetailResp.getDeptPath();
    }
}
