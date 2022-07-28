package com.niulijie.mdm.dto.param;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @ClassName DeviceMailSendParam
 * @Author zhangyingqi
 * describe:设备报表发送邮件请求参数
 * Date 2022/7/15 14:17
 */
@Data
public class DeviceMailSendParam {

    /**
     * 设备状态
     * 0-注销；1-正常；2-禁用 3-注销中
     * */
    @Valid
    @NotEmpty(message = "状态选择不能为空")
    private List<Integer> status ;


}
