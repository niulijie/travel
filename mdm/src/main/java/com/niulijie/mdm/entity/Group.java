package com.niulijie.mdm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zhoupengbing
 * @packageName com.telecomyt.ucenter.entity
 * @email zhoupengbing@telecomyt.com.cn
 * @description
 * @createTime 2019年12月15日 23:41:00 @Version v1.0
 */
@Data
@TableName("uc_group")
public class Group implements Serializable {

  private static final long serialVersionUID = -3730123129113689192L;

  /** 分组ID */
  @TableId(type = IdType.AUTO)
  private int groupId;

  /** 分组名称 */
  @NotBlank(message = "分组名称不能为空")
  private String groupName;

  /** 分组排序 */
  private Integer groupOrder = 1;

  /** 分组状态 */
  private Integer status = 0;

  /** 租戶ID */
  private int tenantId;

  private Date createTime = new Date();

  private Date updateTime = new Date();
}
