package com.niulijie.mdm.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.niulijie.mdm.api.UserApi;
import com.niulijie.mdm.dto.param.DeviceExcelParam;
import com.niulijie.mdm.dto.param.DeviceMailSendParam;
import com.niulijie.mdm.dto.response.DeviceByGroupExport;
import com.niulijie.mdm.entity.Group;
import com.niulijie.mdm.entity.UcUser;
import com.niulijie.mdm.mapper.DeviceMapper;
import com.niulijie.mdm.util.ExcelWidthStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName DeviceMailServiceImpl
 * @Author zhangyingqi
 * describe:
 * Date 2022/7/11 16:34
 */
@Service
@Slf4j
public class DeviceMailService{


    @Resource
    private DeviceMapper deviceMapper;

    /**
     * 终端报表导出 - 动态
     * */
    public void deviceGroupExport(DeviceExcelParam deviceExcelParam, HttpServletResponse response) throws Exception {
        IPage<Map<String,Object>> page = new Page<>(deviceExcelParam.getCurrent(), deviceExcelParam.getSize());
        boolean isNotAdmin = !UserApi.isAdmin();
        // 用户分组ID集合
        List<Integer> groupIds = new ArrayList<>(deviceExcelParam.getGroupId());
        // 查询所有用户
        List<Integer> userIds = Lists.newArrayList();
        // 拼接表头和sql {ParamOne=[[使用者信息], [部门名称]], ParamTwo=[[info.user_name,relOne.dept_path]]}
        Map<String, List<List<String>>> dynamicHeadTemp = this.createDynamicHead(deviceExcelParam);
        // 创建动态表头 [[使用者信息], [部门名称]]
        List<List<String>> dynamicHead = dynamicHeadTemp.get("ParamOne");
        // dynamicHeadTemp.get("ParamTwo").get(0) --> [info.user_name,relOne.dept_path]
        if (!ObjectUtils.isEmpty(dynamicHeadTemp.get("ParamTwo").get(0))) {
            // info.user_name,relOne.dept_path
            String fieldName = dynamicHeadTemp.get("ParamTwo").get(0).get(0);
            // 查询结果： [[张三, 1333333333, 研发中心], [张三, 1333333333, 研发中心]]
            List<List<Object>> createDataByList = Lists.newArrayList();
            if (isNotAdmin && ObjectUtil.isEmpty(userIds)) {
                createDataByList = Collections.emptyList();
            }else {
                page =  deviceMapper.deviceChooseField(page, userIds, deviceExcelParam.getPhone(),deviceExcelParam.getName(),deviceExcelParam.getStatus(),deviceExcelParam.getImei(),fieldName);
                if (ObjectUtils.isEmpty(page.getRecords())) {
                    createDataByList = Collections.emptyList();
                }else {
                    List<Map<String, Object>> dataList =  page.getRecords();
                    // [[张三, 1333333333, 研发中心], [张三, 1333333333, 研发中心]]
                    createDataByList = this.data(dataList);
                }
            }
            // 20220721201830
            String now = DateUtil.now().replaceAll("[[\\s-:punct:]]","");
            String fileName = "终端报表"+now+".xlsx";
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            //内容策略
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            //设置 水平居中
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
            try {
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
                response.setContentType("application/force-download;charset=UTF-8");
                // 这里URLEncoder.encode可以防止中文乱码
                response.addHeader("Content-Disposition","attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
                EasyExcel.write(response.getOutputStream())
                        .head(dynamicHead)
                        .registerWriteHandler(new ExcelWidthStyleStrategy())
                        .registerWriteHandler(horizontalCellStyleStrategy)
                        .autoCloseStream(Boolean.FALSE)
                        .sheet("终端报表")
                        .doWrite(createDataByList);

            } catch (Exception e) {
                log.error("报表导出失败: {}", e.getMessage());
            }
        } else {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "请至少选择一个导出列");
            response.getWriter().println(JSONUtil.toJsonStr(map));
        }
    }

    /**
     * 动态生成表头和查询的动态sql
     * */
    private  Map<String, List<List<String>>> createDynamicHead(DeviceExcelParam deviceExcelParam) {
        // [[使用者信息], [手机号], [部门名称]]
        List<List<String>> list = Lists.newArrayList();
        // 返回参数结果集 {ParamOne=[[使用者信息], [部门名称]], ParamTwo=[[info.user_name,relOne.dept_path]]}
        Map<String, List<List<String>>> map = new HashMap<>(2);
        List<List<String>> sql = new ArrayList<>(1);
        // 查询表字段集合
        StringBuilder fieldName = new StringBuilder();
        if (deviceExcelParam.getUser()) {
            // ["使用者信息"]
            List<String> userName = Lists.newArrayList();
            userName.add("使用者信息");
            list.add(userName);
            fieldName.append("info.user_name,");
        }
        if (deviceExcelParam.getCell()) {
            // ["手机号"]
            List<String> phone = Lists.newArrayList();
            phone.add("手机号");
            list.add(phone);
            fieldName.append("info.phone,");
        }
        if (deviceExcelParam.getPath()) {
            List<String> deptPath = Lists.newArrayList();
            deptPath.add("部门名称");
            list.add(deptPath);
            fieldName.append("relOne.dept_path,");
        }
        if (deviceExcelParam.getDevice()) {
            List<String> name = Lists.newArrayList();
            name.add("终端名称");
            list.add(name);
            fieldName.append("relTwo.name,");
        }
        if (deviceExcelParam.getUnique()) {
            List<String> imei = Lists.newArrayList();
            imei.add("imei");
            list.add(imei);
            fieldName.append("info.imei,");
        }
        if (deviceExcelParam.getCondition()) {
            List<String> status = Lists.newArrayList();
            status.add("设备状态");
            list.add(status);
            // IF函数根据条件的结果为true或false，返回第一个值，或第二个值 语法：IF(condition, value_if_true, value_if_false)
            fieldName.append("  IF(     ( info.STATUS >= 2 ),       IF( ( info.STATUS = 3 ), \"注销中\", \"禁用\" ),      IF( ( info.STATUS = 0 ), \"注销\", \"正常\" )      ) AS status,");
        }
        if (deviceExcelParam.getRegist()) {
            List<String> registType = Lists.newArrayList();
            registType.add("开通方式");
            list.add(registType);
            fieldName.append("IF(( info.regist_type = 0 ), \"服务端开通\", \"客户端注册\" ) AS regist_type,");
        }
        if (deviceExcelParam.getMode()) {
            List<String> modeType = Lists.newArrayList();
            modeType.add("设备模式");
            list.add(modeType);
            fieldName.append("info.mode_type,");
        }
        if (deviceExcelParam.getTime()) {
            List<String> updatedTime = Lists.newArrayList();
            updatedTime.add("最后上报时间");
            list.add(updatedTime);
            fieldName.append("info.updated_time,");
        }
        map.put("ParamOne",list);
        List<String> temp = Lists.newArrayList();
        if (fieldName.length() > 1) {
            temp.add(fieldName.substring(0,fieldName.length()-1));
        }
        sql.add(temp);
        map.put("ParamTwo",sql);

        return map;
    }

    /**
     * 数据转换格式匹配动态表头
     * */
    public List<List<Object>> data (List<Map<String, Object>> mapList) {
        // [[张三, 1333333333, 研发中心], [张三, 1333333333, 研发中心]]
        List<List<Object>> list =  new ArrayList<List<Object>>();
        for (Map<String, Object> map : mapList) {
            // [张三, 1333333333, 研发中心]
            List<Object> objectList = new ArrayList<>();
            if (map.containsKey("user_name")) {
                objectList.add(map.get("user_name"));
            }
            if (map.containsKey("phone")) {
                objectList.add(map.get("phone"));
            }
            if (map.containsKey("dept_path")) {
                objectList.add(map.get("dept_path"));
            }
            if (map.containsKey("name")) {
                objectList.add(map.get("name"));
            }
            if (map.containsKey("imei")) {
                objectList.add(map.get("imei"));
            }
            if (map.containsKey("status")) {
                objectList.add(map.get("status"));
            }
            if (map.containsKey("regist_type")) {
                objectList.add(map.get("regist_type"));
            }
            if (map.containsKey("mode_type")) {
                objectList.add(map.get("mode_type"));
            }
            if (map.containsKey("updated_time")) {
                objectList.add(map.get("updated_time").toString());
            }
            list.add(objectList);
        }
        return list;
    }

    /**
     * 终端报表导出发送邮件
     * */
    public void deviceGroupExportSend(DeviceMailSendParam param, HttpServletResponse response) throws IOException {
        try {
            //查询用户中心所有用户分组
            List<Group> groups = Lists.newArrayList();
            Map<Integer, String> groupsMap = groups.stream().collect(Collectors.toMap(Group::getGroupId, Group::getGroupName));
            Boolean flag = false;
            // 这里URLEncoder.encode可以防止中文乱码
            String fileName = "终端报表信息.xlsx";
            Integer count = 0;
            response.setContentType("application/octet-stream; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName + ";filename*=utf-8''" + URLEncoder.encode(fileName, "utf-8"));
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
            if (!ObjectUtils.isEmpty(groupsMap)) {
                for (Integer id : groupsMap.keySet()) {
                    List<UcUser> members = Lists.newArrayList();
                    List<Integer> userIds = members.stream().map(UcUser::getUserId).collect(Collectors.toList());
                    List<DeviceByGroupExport> data = new ArrayList<>();
                    if (!ObjectUtils.isEmpty(userIds)) {
                        data = deviceMapper.deviceAllExportByGroup(param.getStatus(), userIds);
                        if (!ObjectUtils.isEmpty(data)) {
                            flag = true;
                        }else {
                            //查询设备数据不存在
                            data = Collections.emptyList();
                        }
                    }else {
                        //该分组无用户
                        data = Collections.emptyList();
                    }
                    WriteSheet mainSheet = EasyExcel.writerSheet(count, groupsMap.get(id)).head(DeviceByGroupExport.class).build();
                    excelWriter.write(data,mainSheet);
                    count += 1;
                }
            }
            if (!flag) {
                response.addHeader("errorCode","2006");
                response.addHeader("reason",URLEncoder.encode("终端报表无数据可导出", "utf-8"));
            }
            excelWriter.finish();
        }
        catch (Exception e) {
            log.error("终端报表信息导出失败: {}", e.getMessage());
            response.addHeader("errorCode","2005");
        }
    }

    public static void main(String[] args) {
        // 返回参数结果集 {ParamOne=[[使用者信息], [部门名称]], ParamTwo=[[info.user_name,relOne.dept_path]]}
        Map<String, List<List<String>>> map = new HashMap<>(2);
        // [[使用者信息], [手机号], [部门名称]]
        List<List<String>> list = Lists.newArrayList();
        // 查询表字段集合
        StringBuilder fieldName = new StringBuilder();
        // [[info.user_name,relOne.dept_path]]
        List<List<String>> sql = new ArrayList<>(1);

        List<String> userName = Lists.newArrayList();
        userName.add("使用者信息");
        list.add(userName);
        fieldName.append("info.user_name,");
        List<String> deptPath = Lists.newArrayList();
        deptPath.add("部门名称");
        list.add(deptPath);
        fieldName.append("relOne.dept_path,");
        map.put("ParamOne",list);
        List<String> temp = Lists.newArrayList();
        if (fieldName.length() > 1) {
            temp.add(fieldName.substring(0,fieldName.length()-1));
        }
        sql.add(temp);
        map.put("ParamTwo",sql);

        System.out.println("list:"+list);
        System.out.println("fieldName:"+fieldName);
        System.out.println("sql:"+sql);
        System.out.println("map:"+map);


        String now = DateUtil.now().replaceAll("[[\\s-:punct:]]","");
        System.out.println("now:"+now);
    }
}
