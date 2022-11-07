package com.niulijie.easyexcel.controller;

import com.niulijie.easyexcel.pojo.entity.User;
import com.niulijie.easyexcel.pojo.entity.UserExcel;
import com.niulijie.easyexcel.utils.DynamicExcelUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/test")
@Slf4j
//@RequiredArgsConstructor
public class TestExcelController {

    @Autowired
    private DynamicExcelUtils excelUtils;

    @GetMapping("v1/test01.do")
    @CrossOrigin
    //@ApiOperation(value = "测试动态模板生成下载")
    public void test01(HttpServletResponse response) {
        //标题头数据
        String title =  "说明：\n"+
                "1、带*号的字段必填\n"+
                "2、员工工号只能输入英文和数字，长度限制10。\n"+
                "3、员工姓名长度限制20。\n"+
                "4、下拉框选项只能选择模板提供的下拉选项。\n"+
                "5、年龄只能输入数字。\n"+
                "6、第三行数据为示例数据，可以删除输入正式数据。\n"+
                "7、导入表格时本说明不要删除。";
        //需要生成的excel列字段名称
        List<String> names = Arrays.asList("姓名","工号","年龄","下拉列1","性别");
        //设置第三行示例数据
        List<User> userList = new ArrayList<>();
        User user = new User("张三", "B112233", "18", "下拉值01", "男");
        userList.add(user);
        //列字段的英文名称,根据英文名称对应示例数据
        List<String> fieldEn = Arrays.asList("name","workCode","age","select","sex");

        List<Map<Integer, List<String>>> selectMapList = new ArrayList<>();
        //设置第几列显示下拉框，用户只能选择下拉框中的值，非下拉框则不允许保存excel
        Map<Integer, List<String>> selectMap = new HashMap<>(8);
        //设置下拉框的值
        List<String> selectList = Arrays.asList("下拉字段1", "下拉字段2", "下拉字段3");
        //设置excel第几列为下拉列
        selectMap.put(3,selectList);

        /*Map<Integer, List<String>> selectSexMap = new HashMap<>(8);
        selectSexMap.put(4, Arrays.asList("男", "女"));

        selectMapList.add(selectMap);
        selectMapList.add(selectSexMap);*/

        //设置sheet页名称
        String sheetName = "测试sheet页";
        //设置文件名称
        String fileName = "测试生成动态模板";
        //以上所有数据都是支持动态设置的
        excelUtils.excelDownloadLink(response,title,names,fieldEn,userList,selectMap,sheetName,fileName);
        //excelUtils.excelDownloadLink1(response,title,names,fieldEn,userList,selectMapList,sheetName,fileName);
    }

    @GetMapping("v1/test04.do")
    @CrossOrigin
    //@ApiOperation(value = "测试动态模板生成下载")
    public void test04(HttpServletResponse response) {
        //标题头数据
        String title =  "说明：\n"+
                "1、带*号的字段必填\n"+
                "2、员工工号只能输入英文和数字，长度限制10。\n"+
                "3、员工姓名长度限制20。\n"+
                "7、导入表格时本说明不要删除。";
        //需要生成的excel列字段名称
        List<String> names = Arrays.asList("姓名","工号","年龄","下拉列1","性别");
        //设置第三行示例数据
        List<User> userList = new ArrayList<>();
        User user = new User("张三", "B112233", "18", "下拉值01", "男");
        userList.add(user);
        //列字段的英文名称,根据英文名称对应示例数据
        List<String> fieldEn = Arrays.asList("name","workCode","age","select","sex");

        List<Map<Integer, List<String>>> selectMapList = new ArrayList<>();
        //设置第几列显示下拉框，用户只能选择下拉框中的值，非下拉框则不允许保存excel
        Map<Integer, List<String>> selectMap = new HashMap<>(8);
        //设置下拉框的值
        List<String> selectList = Arrays.asList("下拉字段1", "下拉字段2", "下拉字段3");
        //设置excel第几列为下拉列
        selectMap.put(3,selectList);

        /*Map<Integer, List<String>> selectSexMap = new HashMap<>(8);
        selectSexMap.put(4, Arrays.asList("男", "女"));

        selectMapList.add(selectMap);
        selectMapList.add(selectSexMap);*/

        //设置sheet页名称
        String sheetName = "测试4sheet页";
        //设置文件名称
        String fileName = "测试4生成动态模板";
        //以上所有数据都是支持动态设置的
        //excelUtils.excelDownloadLink(response,title,names,fieldEn,userList,selectMap,sheetName,fileName);
        excelUtils.excelDownloadLink1(response,title,names,fieldEn,userList,selectMapList,sheetName,fileName);
    }

    @GetMapping("v1/test02.do")
    @CrossOrigin
    //@ApiOperation(value = "测试excel导出动态数据")
    public void test02(HttpServletResponse response) {
        //自定义需要导出的excel列字段名称
        List<String> names = Arrays.asList("姓名(自定义)","工号（自定义）","年龄（自定义名称）","下拉列（自定义名称）");
        //列字段的英文名称,根据英文名称对应示例数据
        List<String> fieldEn = Arrays.asList("name","workCode","age","select");
        //要导出的数据
        List<User> userList = new ArrayList<>();
        /*User user1 = new User("张三01", "B11", "18", "下拉值01");
        User user2 = new User("张三02", "B112", "19", "下拉值02");
        User user3 = new User("张三03", "B11223", "20", "下拉值03");
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);*/
        //设置sheet页名称
        String sheetName = "测试sheet页";
        //设置文件名称
        String fileName = "测试导出动态excel数据";
        excelUtils.excelExportData(response,names,fieldEn,userList,sheetName,fileName);

    }

    @GetMapping("v1/test03.do")
    @CrossOrigin
    //@ApiOperation(value = "测试excel导出普通通用实体类型数据")
    public void test03(HttpServletResponse response) {

        List<UserExcel> userExcels = new ArrayList<>();
        userExcels.add(new UserExcel("张01","张测试1","B001","16","下拉01"));
        userExcels.add(new UserExcel("张02","张测试2","B002","17","下拉02"));
        userExcels.add(new UserExcel("张03","张测试3","B003","18","下拉03"));
        //设置sheet页名称
        String sheetName = "测试sheet页";
        //设置文件名称
        String fileName = "测试导出excel数据";
        excelUtils.excelExportOrdinaryData(response,sheetName,fileName,userExcels,UserExcel.class);
    }
}
