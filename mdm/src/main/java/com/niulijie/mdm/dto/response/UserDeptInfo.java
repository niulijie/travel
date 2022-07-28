package com.niulijie.mdm.dto.response;

import lombok.Getter;
import lombok.Setter;


/**
 * @author zhoupengbing
 * @packageName com.telecomyt.ucenter.entity
 * @email zhoupengbing@telecomyt.com.cn
 * @description 用户部门关系
 * @createTime 2019年12月11日 22:20:00
 * @Version v1.0
 */
@Setter
@Getter
public class UserDeptInfo {
    
    /**
     * 用户ID
     */
    private Integer userId;

    private Integer deptId;

    /**
     * 部门ID
     */
    private String deptLevel;

}
