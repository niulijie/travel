package com.niulijie.mdm.controller;

import com.niulijie.mdm.dto.param.DeviceExcelParam;
import com.niulijie.mdm.dto.param.DeviceMailSendParam;
import com.niulijie.mdm.service.DeviceMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


/**
 * @ClassName DeviceMailController
 * @Author zhangyingqi
 * describe:统计设备一些信息发送邮件
 * Date 2022/7/11 10:44
 */
@RestController
@RequestMapping("/device/state")
public class DeviceMailController {


    @Autowired
    private DeviceMailService deviceMailService;

    /**
     * 通过分组管理的用户查询设备列表的导出功能
     */
    @RequestMapping(value ="/export")
    public void sensitiveWordExport(DeviceExcelParam deviceExcelParam, HttpServletResponse response) throws Exception{
        deviceMailService.deviceGroupExport(deviceExcelParam,response);
    }

    /**
     * 邮箱发送报表
     */
    @RequestMapping(value ="/export/stream")
    public void sensitiveWordExportSend(@RequestBody DeviceMailSendParam param, HttpServletResponse response) throws Exception{
        deviceMailService.deviceGroupExportSend(param,response);
    }




}
