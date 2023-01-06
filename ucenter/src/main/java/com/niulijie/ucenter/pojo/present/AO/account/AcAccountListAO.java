package com.niulijie.ucenter.pojo.present.AO.account;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.niulijie.ucenter.common.PageBase;
import com.niulijie.ucenter.validate.SqlTransformDeserializer;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author dcs
 * @description 查询账号列表参数
 * @createTime 2021.8.17
 */
@Data
public class AcAccountListAO extends PageBase {
    /**
     * 账号名
     */
    @JsonDeserialize(using = SqlTransformDeserializer.class)
    private String accountName;

    /**
     * 用户名
     */
    @JsonDeserialize(using = SqlTransformDeserializer.class)
    private String userName;

    /**
     * 用户ID集合
     */
    private List<Integer> userIds;

    /**
     * 创建者名
     */
    @JsonDeserialize(using = SqlTransformDeserializer.class)
    private String creatorName;

    /**
     * 账号类型：0-默认，1-自定义
     */
    private Integer accountType;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 部门ID
     */
    private Integer deptId;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 账号组ID
     */
    private Integer groupId;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 查询类型：0:创建时间查询(默认),1:关联时间查询
     */
    private Integer queryType = 0;

    /**
     * 账号状态: 0-启用, 1-停用, 2-删除
     */
    private Integer status;

    /**
     * 部门id列表（支持新华社项目）
     */
    private List<Integer> deptIds;
}
