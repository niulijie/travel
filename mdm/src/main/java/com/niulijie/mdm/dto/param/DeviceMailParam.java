package com.niulijie.mdm.dto.param;

import com.niulijie.mdm.result.PageBase;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName DeviceMailParam
 * @Author zhangyingqi
 * describe: 按照用户组和查询条件筛选处设备进行邮件发送和导出请求类
 * Date 2022/7/11 11:12
 */
@Data
public class DeviceMailParam extends PageBase implements Serializable {

    private static final long serialVersionUID = 5344838598104573938L;

    /** 姓名 */
    private String name;

    /**
     * 设备状态 默认正常,
     * 0-注销；1-正常；2-禁用 3-注销中,null-全部
     * */
    private Integer status = 1;

    /** 用户分组ID集合 */
    @Valid
    @NotNull(message = "用户分组ID不能为空")
    private Integer groupId;

    /** 手机号 */
    private String phone;

    /** imei */
    private String imei;
}
