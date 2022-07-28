package com.niulijie.mdm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author zhoupengbing
 * @packageName com.niulijie.mdm.entity
 * @email zhoupengbing@telecomyt.com.cn
 * @description 设备信息
 * @createTime 2020年02月06日 21:15:00 @Version v1.0
 */
@Data
@TableName("device_info")
public class Device {

  @TableId(type = IdType.AUTO)
  private Integer deviceId;

  private Integer userId;

  private String userName;

  private String modeType;

  private Integer deptId;

  private Integer deviceMid;

  /** 手机PIN码 */
  private String pin;

  /** 手机号码 */
  private String phone;

  /** 绑定设备（IMEI） */
  private String imei;

  /** 用户识别码 */
  private String imsi;

  /** 设备识别码 */
  private String meid;

  /** 注册状态：0-未注册；1-已注册；2-撤销注册 */
  private Integer register;

  /** 添加人 */
  private Integer creator;

  /** 系统版本号 */
  private String system;
  /**
   * 开通方式 0-服务端开通，1-客户端注册
   */
  private Integer registType;

  /** 状态：0-注销；1-正常；2-禁用 */
  private Integer status;

  private Date createdTime;

  private Date updatedTime;
}
