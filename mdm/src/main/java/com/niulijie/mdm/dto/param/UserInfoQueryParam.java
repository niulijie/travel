package com.niulijie.mdm.dto.param;

import lombok.Data;

/**
 * <p>
 * 用户信息查询实体类
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Data
public class UserInfoQueryParam {

    /**
     * 用户姓名/身份证号
     */
    private String searchKey;

    /**
     * 开始的页数
     */
    private Integer current = 1;

    /**
     * 每页数量
     */
    private Integer size = 10;
}
