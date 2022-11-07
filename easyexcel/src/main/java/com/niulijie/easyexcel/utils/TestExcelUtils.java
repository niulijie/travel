package com.niulijie.easyexcel.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.niulijie.easyexcel.strategy.SelectedCellWriteHandler;
import org.apache.commons.codec.CharEncoding;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class TestExcelUtils<T> {

    public static final String CONTENT_TYPE_STREAM = "application/octet-stream";

    public static final String CONTENT_DISPOSITION_ATTACHMENT = "attachment;filename=";

    public static <T> void writeKolWithSheet(String fileName, String sheet, List<T> data, Class<T> obj, HttpServletResponse response){
        try{
            //设置响应头类型
            response.setContentType(CONTENT_TYPE_STREAM);
            //设置编码
            response.setCharacterEncoding(CharEncoding.UTF_8);

            fileName = URLEncoder.encode(fileName + ".xlsx","UTF-8");

            //设置响应头
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, CONTENT_DISPOSITION_ATTACHMENT + fileName);
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();

            WriteSheet writeSheet = EasyExcel.writerSheet(0, sheet)
                    .head(obj)
                    .registerWriteHandler(new SelectedCellWriteHandler())
                    .build();
            excelWriter.write(new ArrayList<>(), writeSheet);
            //excelWriter.write(data,writeSheet);
            excelWriter.finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

