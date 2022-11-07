package com.niulijie.easyexcel.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.niulijie.easyexcel.strategy.CustomSheetWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;

/**
 * @program: sharing-backstage
 * @Description: 动态excel工具类
 * @Author: zwx
 * @Date: 2022/5/12 13:42
 */
@Component
@Slf4j
public class DynamicExcelUtils {

    /**
     * 动态excel模板下载
     * @param response  响应
     * @param title     大标题说明，多行使用转义符\n换行
     * @param names     列集合
     * @param fieldEn   列英文名称
     * @param selectMap 下拉框
     * @param fileName  自定义文件名称
     * @param sheetName 自定义sheet页名称
     */
    public void excelDownloadLink(HttpServletResponse response, String title, List<String> names, List<String> fieldEn , List list , Map<Integer, List<String>> selectMap, String sheetName, String fileName) {
        try {
            //设置表格第三行的示例数据的值
            List<List<String>> datas = setData(list,fieldEn);
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + "." + ExcelTypeEnum.XLSX);
            response.setCharacterEncoding("UTF-8");
            EasyExcel.write(response.getOutputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(head(CollectionUtils.isNotEmpty(names) ? names.toArray(new String[0]) : new String[0], title))
                    //开启内存模式才能使用动态设置标题样式
                    .inMemory(true)
                    .registerWriteHandler(new TitleStyleUtils(names))
                    //.registerWriteHandler(new SelectSheetWriteHandler(selectMap))
                    .registerWriteHandler(new CustomSheetWriteHandler(selectMap))
                    .registerWriteHandler(new SimpleColumnWidthStyleStrategy(25))
                    .sheet(sheetName)
                    .doWrite(CollectionUtils.isNotEmpty(datas) ? datas : null);
        } catch (IOException e) {
            e.printStackTrace();
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            try {
                response.getWriter().println("打印失败");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void excelDownloadLink1(HttpServletResponse response, String title, List<String> names, List<String> fieldEn , List list , List<Map<Integer, List<String>>> selectMapList, String sheetName, String fileName) {
        try {
            //设置表格第三行的示例数据的值
            List<List<String>> datas = setData(list,fieldEn);
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + "." + ExcelTypeEnum.XLSX);
            response.setCharacterEncoding("UTF-8");
            EasyExcel.write(response.getOutputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(head(CollectionUtils.isNotEmpty(names) ? names.toArray(new String[0]) : new String[0], title))
                    //开启内存模式才能使用动态设置标题样式
                    .inMemory(true)
                    .registerWriteHandler(new TitleStyleUtils(names))
                    .registerWriteHandler(new SelectSheetWriteHandler(selectMapList))
                    .registerWriteHandler(new SimpleColumnWidthStyleStrategy(25))
                    .sheet(sheetName)
                    .doWrite(CollectionUtils.isNotEmpty(datas) ? datas : null);
        } catch (IOException e) {
            e.printStackTrace();
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            try {
                response.getWriter().println("打印失败");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 动态excel数据导出
     * @param response 响应
     * @param fileName  自定义文件名称
     * @param sheetName 自定义sheet页名称
     * @param list     数据集合
     */
    public void excelExportData(HttpServletResponse response,List<String> names,List<String> fieldEn,List list ,String sheetName, String fileName){
        try {

            //设置返回数据的值跟动态列一一对应
            List<List<String>> datas = setData(list,fieldEn);
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + "." +ExcelTypeEnum.XLSX);
            response.setCharacterEncoding("UTF-8");
            EasyExcel.write(response.getOutputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(headData(CollectionUtils.isNotEmpty(names) ? names.toArray(new String[0]) : new String[0]))
                    .registerWriteHandler(new AutoWidthHandler())
                    .registerWriteHandler(new SimpleRowHeightStyleStrategy((short) 25, (short) 25))
                    .sheet(sheetName)
                    .doWrite(CollectionUtils.isNotEmpty(datas) ? datas:new ArrayList());

        } catch (IOException e) {
            e.printStackTrace();
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            try {
                response.getWriter().println("打印失败");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 普通excel数据导出(实体类方式)
     * @param response  响应
     * @param fileName  自定义文件名称
     * @param sheetName 自定义sheet页名称
     * @param list     数据集合
     * @param clazz  实体对象字节码对象
     */
    public void excelExportOrdinaryData(HttpServletResponse response,String sheetName, String fileName, List list, Class clazz){
        try {

            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") +"."+ ExcelTypeEnum.XLSX);
            response.setCharacterEncoding("UTF-8");
            EasyExcel.write(response.getOutputStream(),clazz)
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(clazz)
                    //设置默认样式及写入头信息开始的行数
                    .relativeHeadRowIndex(0)
                    .registerWriteHandler(new AutoWidthHandler())
                    .registerWriteHandler(new SimpleRowHeightStyleStrategy((short) 25, (short) 25))
                    .sheet(sheetName)
                    .doWrite(list);
        } catch (IOException e) {
            e.printStackTrace();
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            try {
                response.getWriter().println("打印失败");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


    }

    /**
     * 根据反射构造动态数据
     */
    private List<List<String>> setData(List list,List<String> fieldEn){
        List<List<String>> datas = new ArrayList<>();
        //对象反射转map方法
        List<Map<Object, Object>> maps = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)){
            for (Object o : list) {
                Class<?> aClass = o.getClass();
                Field[] fields = aClass.getDeclaredFields();
                Map<Object, Object> map = new HashMap<>(40);

                for (Field field : fields) {
                    map.put(field.getName(), getResult(field.getName(), o));
                }
                maps.add(map);
            }
            for (Map<Object, Object> map : maps) {
                //用于接收返回数据行？
                List<String> data = new LinkedList<String>();
                for (int i = 0; i < fieldEn.size(); i++) {
                    Object o = map.get(fieldEn.get(i));
                    data.add(Objects.isNull(o) ? "" : o.toString());
                }
                datas.add(data);
            }
        }
        return datas;
    }

    /**
     * 对象实体反射方法
     */
    private static Object getResult(Object fieldName, Object o) {
        try {
            Class<?> aClass = o.getClass();
            Field declaredField = aClass.getDeclaredField(fieldName.toString());
            declaredField.setAccessible(true);
            PropertyDescriptor pd = new PropertyDescriptor(declaredField.getName(), aClass);
            Method readMethod = pd.getReadMethod();

            return readMethod.invoke(o);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 动态头传入 */
    private List<List<String>> head(String[] header, String bigTitle) {
        List<String> head0 = null;
        List<List<String>> list = new LinkedList<List<String>>();
        for (String h : header) {
            head0 = new LinkedList<>();
            head0.add(bigTitle);
            head0.add(h);
            list.add(head0);
        }
        return list;
    }
    /**
     * 数据动态头传入
     */
    private List<List<String>> headData(String[] header) {
        List<String> head0 = null;
        List<List<String>> list = new LinkedList<List<String>>();
        for (String h : header) {
            head0 = new LinkedList<>();
            head0.add(h);
            list.add(head0);
        }
        return list;
    }


}

