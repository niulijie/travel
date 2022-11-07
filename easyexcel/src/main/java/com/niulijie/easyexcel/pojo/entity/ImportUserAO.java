package com.niulijie.easyexcel.pojo.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Map;

/**
 * 企业用户excel导入实体
 * @author sunyi
 * @description 企业用户excel导入实体
 * @createTime 2021年09月02日 19:00:00
 * @Version v2.0
 */
@Data
@Builder
public class ImportUserAO /*extends BaseRowModel*/ {

    /**
     * 用户身份证号
     */
    //@ExcelProperty(index = 0 ,value = "身份证号")
    //@IdCard(regexp = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)",message = "身份证号格式错误")
    @NotBlank(message = "身份证号不能为空")
    private String idCard;

    /**
     * 用户姓名
     */
    //@ExcelProperty(index = 1,value = "用户姓名")
    @Length(min = 1,max = 20, message = "用户姓名为空或者姓名过长")
    private String name;

    /**
     * 用户性别
     */
    //@ExcelProperty(index = 2, value = "用户性别")
    @Pattern(regexp = "(^男|女$)",message = "性别为空或者格式不正确")
    private String sex = "男";

    /**
     * 用户手机号
     */
    //@ExcelProperty(index = 3)
    @Pattern(regexp = "1[3|4|5|6|7|8|9][0-9]\\d{8}",message = "手机号为空或者格式不正确")
    private String telephone;

    /**
     * 用户邮箱
     */
    //@ExcelProperty(index = 4,value = "邮箱")
    @Pattern(regexp = "\\w[-\\w.+]*@([A-Za-z0-9][A-Za-z0-9]+\\.)+[A-Za-z]{2,14}",message = "邮箱格式不正确")
    private String email;

    /**
     * 用户所属部门
     */
    //@ExcelProperty(index = 5,value = "所属企业")
    @NotBlank(message = "所属企业不能为空")
    private String deptName;

    /**
     * 用户
     */
    private Map<String, Object> userAttrs;

}
