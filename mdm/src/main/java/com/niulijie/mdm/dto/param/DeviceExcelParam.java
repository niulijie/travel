package com.niulijie.mdm.dto.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName DeviceExcelEntity
 * @Author zhangyingqi
 * describe: 终端报表动态表头请求参数
 * 默认属性为true为需要的参数列
 * Date 2022/7/12 15:11
 */
@Data
public class DeviceExcelParam extends DeviceMailParam implements Serializable {

    private static final long serialVersionUID = 2194033484735629958L;

    /**
     * 使用者信息
     * */
    private Boolean user;
    /**
     * 手机号
     * */
    private Boolean cell;
    /**
     * 部门名称全路径
     */
    private Boolean path;
    /**
     * 终端名称
     * */
    private Boolean device;
    /**
     * imei
     * */
    private Boolean unique;
    /**
     * 设备状态
     * */
    private Boolean condition;
    /**
     * 开通方式
     */
    private Boolean regist;
    /**
     * 设备模式
     * */
    private Boolean mode;
    /**
     * 最后上报时间
     * */
    private Boolean time;
}
