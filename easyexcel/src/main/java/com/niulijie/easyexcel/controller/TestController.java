package com.niulijie.easyexcel.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.niulijie.easyexcel.annotation.DropDownSetField;
import com.niulijie.easyexcel.pojo.param.StudentExcelExportResult;
import com.niulijie.easyexcel.service.TestService;
import com.niulijie.easyexcel.strategy.CustomSheetWriteHandler2;
import com.niulijie.easyexcel.utils.ResoveDropAnnotationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/download")
    public String windowsClientDownload(HttpServletResponse response, Long procedureId) {
        testService.doDownload(response);
        return "666";
    }

    /**
     * 下载
     * @return
     */
    @GetMapping("/download2")
    public String download(HttpServletResponse response){
        testService.downloadKolList(response);
        return "8888";
    }

    /**
     * 下载学生信息模板 -- 注解版本
     * @param response
     * @param request
     * @throws IOException
     */
    @GetMapping("/download3")
    public void getStudentTemplate(HttpServletResponse response, HttpServletRequest request) throws IOException {

        OutputStream outputStream = response.getOutputStream();
        // 获得模板输入流
        InputStream inputStream = new ClassPathResource("template/export_template.xlsm").getInputStream();
        try {

            String fileName = "学生信息模板";
            String fileName3 = URLEncoder.encode(fileName, "utf-8");
            // 注意导出的文件格式需要是.xlsm
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''"+fileName3+".xlsm");
            response.setContentType("application/msexcel");// 定义输出类型
            response.setCharacterEncoding("UTF-8");

            //获取该类声明的所有字段
            Field[] fields = StudentExcelExportResult.class.getDeclaredFields();
            //响应字段对应的下拉集合
            Map<Integer, String[]> map = new HashMap<>();
            Field field = null;

            // 循环判断哪些字段有下拉数据集，并获取
            for (int i = 0; i < fields.length; i++) {
                field = fields[i];
                // 解析注解信息
                DropDownSetField dropDownSetField = field.getAnnotation(DropDownSetField.class);
                if (null != dropDownSetField) {
                    //调用下拉框数据源的方法
                    String[] source = ResoveDropAnnotationUtil.dynamicListResove(dropDownSetField, field.getName());
                    if (null != source && source.length > 0) {
                        // 记录字段位置，及数据源
                        map.put(i, source);
                    }
                }
            }

            ExcelWriter excelWriter = EasyExcel.write(outputStream)
                    .registerWriteHandler(new CustomSheetWriteHandler2(map))	//使用拦截器填充数据源
                    .withTemplate(inputStream)	// 使用模板输出
                    .build();

            WriteSheet sheet = EasyExcel.writerSheet(0,"Sheet1").build();
            excelWriter.write((Collection<?>) null, sheet);
            excelWriter.finish();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*@GetMapping("/exportUserTemplate")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        //导出模板名称
        String  fileName="测试.xlsx";
        response.setContentType("multipart/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        //编码 防止中文乱码
        try {
            fileName = URLEncoder.encode(fileName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-disposition","attachment;filename="+fileName);
        //excel表头数据
        List<UserDownloadTemplateVO> userDownloadTemplateVOList = new ArrayList<>();
        try {
            //xxxx表示excel下拉框动态数据（一般都是从数据库查出）
            //xxx表示模板里sheet的名称
            EasyExcel.write(response.getOutputStream(),UserDownloadTemplateVO.class)
                    .registerWriteHandler(new CustomSheetWriteHandler(xxxx))
                    .sheet("xxx")
                    .doWrite(userDownloadTemplateVOList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
