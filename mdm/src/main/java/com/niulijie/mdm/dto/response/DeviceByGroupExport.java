package com.niulijie.mdm.dto.response;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.niulijie.mdm.config.EasyExcelLocalDateConverter;
import lombok.Data;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName DeviceByGroupExport
 * @Author zhangyingqi
 * describe:根据用户分组查询设备发送邮件固定列的实体类
 * Date 2022/7/18 9:17
 */
@Data
@HeadStyle(horizontalAlignment = HorizontalAlignment.CENTER)//表头样式
@ContentStyle(horizontalAlignment = HorizontalAlignment.CENTER)//内容样式
@ColumnWidth(20)//表头宽度
public class DeviceByGroupExport implements Serializable {
    @ExcelIgnore
    private Integer userId;
    @ExcelIgnore
    private Integer deptId;

    /**
     * 使用者信息
     */
    @ExcelProperty(index = 0,value = "使用者信息")
    private String userName;

    /**
     * 手机号
     */
    @ExcelProperty(index = 1,value = "手机号")
    private String phone;

    /**
     * 部门名称
     */
    @ExcelProperty(index = 2,value = "部门名称")
    private String deptPath;

    /**
     * 终端名称
     */
    @ExcelProperty(index = 3,value = "终端名称")
    private String name;

    /**
     * imei
     */
    @ExcelProperty(index = 4,value = "imei")
    private String imei;

    /**
     * 设备状态
     */
    @ExcelProperty(index = 5,value = "设备状态")
    private String status;

    /**
     * 开通方式
     */
    @ExcelProperty(index = 6,value = "开通方式")
    private String registType;

    /**
     * 设备模式
     */
    @ExcelProperty(index = 7,value = "设备模式")
    private String modeType;

    /**
     * 最后上报时间
     */
    @ExcelProperty(index = 8,value = "最后上报时间",converter = EasyExcelLocalDateConverter.class)
    private LocalDateTime updatedTime;

}
