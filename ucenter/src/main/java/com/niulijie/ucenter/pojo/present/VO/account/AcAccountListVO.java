package com.niulijie.ucenter.pojo.present.VO.account;

import com.niulijie.ucenter.pojo.entity.AcAccount;
import lombok.Data;

import java.util.List;

/**
 * @author dcs
 * @description 账号列表信息
 * @createTime 2021.8.17
 */
@Data
public class AcAccountListVO extends AcAccount {
    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户状态
     */
    private Integer userStatus;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 创建者
     */
    private String creatorName;

    /**
     * 账号所属部门列表
     */
    private List<AcAccountDeptVO> depts;

    /**
     * 账号所属角色列表
     */
    private List<AcAccountRoleVO> roles;

    /**
     * 账号所属账号组列表
     */
    private List<AcAccountGroupVO> groups;

}
