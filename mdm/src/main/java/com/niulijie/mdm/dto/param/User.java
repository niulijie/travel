package com.niulijie.mdm.dto.param;

import com.niulijie.mdm.dto.enums.ActionTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * @author niuli
 */
@Data
public class User {

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户头像下载地址
     */
    private String avatar;

    private String phone;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 密码
     */
    private String password;

    /**
     * 所属部门ID
     */
    private List<Integer> deptIds;

    /**
     * 数据变更类型 {@link ActionTypeEnum}
     */
    private Integer actionType;

    /**
     * 用户状态 0 启用 1 停用 2 删除
     */
    private Integer status;
}
