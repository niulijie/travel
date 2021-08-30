package com.niulijie.spring.annotation.dto;

import com.niulijie.spring.annotation.Phone;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author zhoupengbing
 * @packageName com.telecomyt.mdm.device.dto
 * @email zhoupengbing@telecomyt.com.cn
 * @description 绑定人员设备参数
 * @createTime 2020年02月07日 15:22:00 @Version v1.0
 */
@Data
public class BindUserDeviceParam {

    @NotNull(message = "用户信息不能为空")
    private Integer userId;

    @NotBlank(message = "用户名不能为空")
    private String userName;

    @NotNull(message = "部门id不能为空")
    private Integer deptId;

    @NotBlank(message = "部门名称不能为空")
    private String deptName;

    @NotBlank(message = "部门路径不能为空")
    private String deptPath;

    @Phone(message = "手机号格式不正确")
    private String phone;

    @Size(min = 1, max = 6, message = "PIN码长度1-6个字符")
    @NotBlank(message = "手机PIN码不能为空")
    private String pin;

    /**
     * imei格式 正整数 不能为空
     */
    @Pattern(regexp = "^[+]{0,1}(\\d+)$", message = "设备IMEI格式不正确")
    @Size(min = 15, max = 17, message = "设备imei为15-17位数字")
    @NotBlank(message = "设备Imei不能为空")
    private String imei;

    @Pattern(regexp = "^[+]{0,1}(\\d+)$", message = "设备IMSI号码格式不正确")
    @Size(min = 15, max = 17, message = "IMSI号码为15-17为数字")
    @NotNull(message = "IMSI号码不能为空")
    private String imsi;

    @Pattern(regexp = "^[a-z0-9A-Z]+$", message = "设备MEID号码格式不正确")
    @Size(min = 14, max = 17, message = "MEID为14-17位字符")
    @NotBlank(message = "MEID号码不能为空")
    private String meid;

    @NotNull(message = "设备型号信息不能为空")
    private Integer deviceModelId;

    @NotNull(message = "creator不能为空")
    private Integer creator;
}
