package com.niulijie.mdm.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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
public class UserDept implements Serializable {
    
    private static final long serialVersionUID = -7610678622437445745L;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 部门ID
     */
    private String deptPath = "";

}
