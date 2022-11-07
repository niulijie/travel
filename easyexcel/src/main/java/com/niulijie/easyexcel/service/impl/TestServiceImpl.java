package com.niulijie.easyexcel.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.niulijie.easyexcel.mapper.UcUserAttrMapper;
import com.niulijie.easyexcel.pojo.entity.ImportUserAO;
import com.niulijie.easyexcel.pojo.entity.UcUserAttr;
import com.niulijie.easyexcel.pojo.param.HeadAttrSimple;
import com.niulijie.easyexcel.pojo.param.TestExport;
import com.niulijie.easyexcel.service.TestService;
import com.niulijie.easyexcel.strategy.CellCustomWriteHandler;
import com.niulijie.easyexcel.strategy.CellHeadStyleStrategy;
import com.niulijie.easyexcel.strategy.CellRowHeightStyleStrategy;
import com.niulijie.easyexcel.strategy.CellRowWidthStyleStrategy;
import com.niulijie.easyexcel.utils.TestExcelUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private UcUserAttrMapper ucUserAttrMapper;

    @Override
    public void doDownload(HttpServletResponse response) {
        //设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        //中文文件名编码
        String fileName = null;
        try {
            //中文文件名编码要用URLEncoder.encode编码
            fileName = URLEncoder.encode("模板", "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName + ".xlsx");
        try {
            //主标题和副标题在excel中分别是是第0和第1行
            List<Integer> columnIndexes = Arrays.asList(0,1);
            //自定义标题和内容策略(具体定义在下文)
            //模板属性集合
            List<HeadAttrSimple> attrs = new ArrayList<>();

            // 查询企业用户的自定义字段
            List<UcUserAttr> userAttrs = ucUserAttrMapper.selectList(  new QueryWrapper<UcUserAttr>() .lambda()
                    .eq(UcUserAttr::getTenantId, 184)
                    .eq(UcUserAttr::getStatus, 0)
                    .orderByAsc(UcUserAttr::getAttrOrder));
            // 是否有自定义属性
            if (!CollectionUtils.isEmpty(userAttrs)) {
                userAttrs.stream()
                        .forEach(
                                userAttr -> {
                                    HeadAttrSimple userAttrSimple = new HeadAttrSimple();
                                    BeanUtils.copyProperties(userAttr, userAttrSimple);
                                    attrs.add(userAttrSimple);
                                });
            }

            //表头1 - 固定
            String title = "填写说明:\r\n" +
                    "1、请勿更改模板的格式\r\n" +
                    "2、红色标识的字段都是必填字段，为空数据无法正确导入[编号为行号，数字标识]\r\n" +
                    "3、所属部门填写说明：A>B;A>C; A为企业根部门名称，B和C是用户所在的两个部门\r\n" +
                    "4、最后一行数据后的其他行，请用删除方式删除干净，以免出现误读情况";
            //表头2 - 动态查询属性列表
            String[] strings = attrs.stream().map(HeadAttrSimple::getAttrName).toArray(String[]::new);

            //列字段的英文名称,根据英文名称对应示例数据
            List<String> fieldEn = attrs.stream().map(HeadAttrSimple::getAttrField).collect(Collectors.toList());

            //数据
            ArrayList<List<Object>> dataList = new ArrayList<>();
            ImportUserAO build = ImportUserAO.builder().idCard("134731199211111210").name("王五").sex("女").telephone("13488777776")
                    .email("1223@qq.com").deptName("新基线>销售部").build();
            ArrayList<Object> list = new ArrayList<>();
            list.add(build);
            dataList.add(list);

            //设置表格第三行的示例数据的值
            List<List<String>> datas = setData(list,fieldEn);

            CellHeadStyleStrategy cellHeadStyleStrategy = new CellHeadStyleStrategy(columnIndexes, attrs, new WriteCellStyle(), new WriteCellStyle());

            EasyExcel.write(response.getOutputStream())
                    .head(head(strings, title))
                    //开启内存模式才能使用动态设置标题样式
                    .inMemory(true)
                    //设置列宽的策略
                    .registerWriteHandler(new CellRowWidthStyleStrategy())
                    //设置行高的策略
                    .registerWriteHandler(new CellRowHeightStyleStrategy())
                    //设置表头和内容的策略
                    .registerWriteHandler(cellHeadStyleStrategy)
                    //下拉框策略
                    .registerWriteHandler(new CellCustomWriteHandler(attrs))
                    .sheet("测试")
                    .doWrite(datas);
                    //.build();

            //填入数据
            //writeData(excelWriter, attrs);
            // 千万别忘记关闭流
            //excelWriter.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 写入数据
     * @param excelWriter excelWriter
     * @param attrs
     */
    private void writeData(ExcelWriter excelWriter, List<HeadAttrSimple> attrs){

        WriteSheet writeSheet = new WriteSheet();
        //设置写到第几个sheet
        writeSheet.setSheetNo(0);
        writeSheet.setSheetName("测试");

        //设置表头
        List<List<String>> headList = new ArrayList<>();
        String name = "填写说明:\r\n" +
                "1、请勿更改模板的格式\r\n" +
                "2、红色标识的字段都是必填字段，为空数据无法正确导入[编号为行号，数字标识]\r\n" +
                "3、所属部门填写说明：A>B;A>C; A为企业根部门名称，B和C是用户所在的两个部门\r\n" +
                "4、最后一行数据后的其他行，请用删除方式删除干净，以免出现误读情况";
        /*headList.add(Arrays.asList(name,"序号"));
        headList.add(Arrays.asList(name,"名称"));
        for (int i = 1; i <3 ; i++) {
            headList.add(Arrays.asList(name,"单位"+i));
        }*/
        for (HeadAttrSimple attr : attrs) {
            headList.add(Arrays.asList(name,attr.getAttrName()));
        }
        writeSheet.setHead(headList);

        //（设置数据）
        //第一列序号从1开始增加
        AtomicInteger orderNumber = new AtomicInteger(1);
        ArrayList<List<Object>> dataList = new ArrayList<>();
        ImportUserAO build = ImportUserAO.builder().idCard("134731199211111210").name("王五").sex("女").telephone("13488777776")
                .email("1223@qq.com").deptName("新基线>销售部").build();
        /*for (int i = 0; i < 10; i++) {
            List<Object> data = ListUtils.newArrayList();
            data.add(String.valueOf(orderNumber.getAndIncrement()));
            data.add("名称" + i);
            data.add("单元"+i);
            data.add(0.56);
            dataList.add(data);
        }*/
        ArrayList<Object> list = new ArrayList<>();
        list.add(build);
        dataList.add(list);
        excelWriter.write(dataList, writeSheet);
    }

    @Override
    public void downloadKolList(HttpServletResponse response) {
        List<TestExport> list = new ArrayList<>();
        TestExport export = new TestExport();
        export.setKey("A");
        export.setValue("1");


        TestExport export1 = new TestExport();
        export1.setKey("B");
        export1.setValue("2");

        list.add(export);
        list.add(export1);

        String fileName = "数据清单";
        String sheet = "数据清单";
        List<ImportUserAO> list2 = new ArrayList<>();
        ImportUserAO build1 = ImportUserAO.builder().idCard("134731199211111210").name("王五").sex("女").telephone("13488777776")
                .email("1223@qq.com").deptName("新基线>销售部").build();
        list2.add(build1);
        //TestExcelUtils.writeKolWithSheet(fileName,sheet, list,TestExport.class,response);
        TestExcelUtils.writeKolWithSheet(fileName,sheet, list2,ImportUserAO.class,response);
    }

    /**
     * 根据反射构造动态数据
     */
    private List<List<String>> setData(List list,List<String> fieldEn){
        List<List<String>> datas = new ArrayList<>();
        //对象反射转map方法
        List<Map<Object, Object>> maps = new ArrayList<>();
        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(list)){
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

    /**
     * 数据动态头传入
     */
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
}
