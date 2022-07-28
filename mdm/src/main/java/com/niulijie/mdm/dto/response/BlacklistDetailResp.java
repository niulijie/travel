package com.niulijie.mdm.dto.response;

import lombok.Data;

import java.time.LocalDate;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Data
public class BlacklistDetailResp implements IBatchSetUser{

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 部门id
     */
    private Integer deptId;

    /**
     * 部门全路径
     */
    private String deptPath;

    /**
     * 受限类型（黑名单） 0-不受限、1-不能发视频	2-不能发评论、3-不能发视
     */
    private String limitType;

    /**
     * 受限类型名称（黑名单） 0-不受限、1-不能发视频	2-不能发评论、3-不能发视
     */
    private String limitTypeName;

    /**
     * 受限结束时间
     */
    private LocalDate limitTime;

    /**
     * 获取用户id
     *
     * @return
     */
    @Override
    public Integer batchGetUserId() {
        return userId;
    }

    /**
     * 设置用户信息的方法
     *
     * @param userDetailResp
     */
    @Override
    public void batchSetUser(UserDetailResp userDetailResp) {
        this.deptId = userDetailResp.getDeptId();
        this.deptPath = userDetailResp.getDeptPath();
    }
}
