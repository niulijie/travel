package com.niulijie.mdm.dto.response;

import lombok.Data;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Data
public class UserDetailResp {

    /**
     * 用户id
     */
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
     * 部门id
     */
    private Integer deptId;

    /**
     * 部门全路径
     */
    private String deptPath;

}
