package com.niulijie.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhoupengbing
 * @packageName com.telecomyt.ucenter.entity
 * @email zhoupengbing@telecomyt.com.cn
 * @description 部门
 * @createTime 2019年12月11日 15:04:00 @Version v1.0
 */
@Data
public class UcDept implements Serializable {

  private static final long serialVersionUID = 8720614748632896760L;

  /** 部门ID */
  private int deptId;

  /** 部门名称 */
  @NotBlank(message = "部门名称不能为空")
  private String deptName;

  /** 上级部门ID */
  private int parentId;

  /** 部门等级 */
  private String deptLevel;

  /** 部门全路径 */
  private String deptPath;

  /** 部门排序 */
  private Integer deptOrder;

  /** 部门状态 */
  private int status;

  /** 租户ID */
  private int tenantId;

  /** 创建时间 */
  private Date createTime;

  /** 更新时间 */
  private Date updateTime = new Date();

  /** 上级部门信息 */
  //private UcDept parent;

  /** 部门的自定义属性信息 */
  @TableField(exist = false)
  private Map<String, Object> items;

  @TableField(exist = false)
  private List<UcDept> childrenList;
}
