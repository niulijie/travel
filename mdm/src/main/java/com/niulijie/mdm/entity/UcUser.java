package com.niulijie.mdm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 人员信息表
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Data
@TableName("uc_user")
public class UcUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，用户id
     */
    @TableId(value = "user_id", type = IdType.INPUT)
    private Integer userId;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 签名
     */
    private String autograph;

    /**
     * 性别,男女
     */
    private String sex;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 工号
     */
    private String userNo;

    /**
     * 职务
     */
    private String title;

    /**
     * 喜欢数（点赞数）
     */
    private Integer likeCount;

    /**
     * 收藏数
     */
    private Integer collectCount;

    /**
     * 作品数
     */
    private Integer videoCount;

    /**
     * 审核本部门视频的权限  1 默认有 2 没有
     */
    private Integer auditAuth;

    /**
     * 受限类型（黑名单） 0-不受限、1-不能发视频	2-不能发评论、3-不能发视
     */
    private String limitType;

    /**
     * 受限结束时间
     */
    private LocalDate limitTime;

    /**
     * 用户状态 0 启用 1 停用 2 删除
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
