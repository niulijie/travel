package com.niulijie.ucenter.pojo.present.VO.account;

import lombok.Data;

import java.util.Date;

@Data
public class AcAccountGroupVO {
    /**
     * 主键ID
     */
    private Integer groupId;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 创建者
     */
    private Integer creator;

    /**
     * 创建时间
     */
    private Date createdTime;

}
