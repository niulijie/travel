package com.niulijie.easyexcel.utils;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;
import org.apache.poi.ss.usermodel.*;

import java.util.List;

/**
 * @program: sharing-backstage
 * @Description: 标题样式工具类
 * @Author: zwx
 * @Date: 2022/2/21 16:34
 */
public class TitleStyleUtils extends AbstractCellStyleStrategy {

    private List<String> names;

    //private Workbook workbook;

    public TitleStyleUtils(){

    }

    public TitleStyleUtils (List<String> names){
        this.names = names;
    }

    /*protected void initCellStyle(Workbook workbook) {
        this.workbook = workbook;
    }*/

    @Override
    protected void setHeadCellStyle(Cell cell, Head head, Integer relativeRowIndex) {
        //设置标题说明样式（excel第一行，多个换行符拼接的标题说明文字）
        Workbook workbook = cell.getSheet().getWorkbook();
        if(cell.getColumnIndex()==0 && relativeRowIndex==0){
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);
            //设置 水平居中
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            //设置 垂直居中
            cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
            Font font = workbook.createFont();
            //颜色
            font.setColor(IndexedColors.RED.getIndex());
            //加粗
            font.setBold(true);
            //字体
            font.setFontName("宋体");
            //大小
            font.setFontHeightInPoints((short) 12);
            //高度
            workbook.getSheetAt(0).getRow(0).setHeight((short) 3888);
            //宽度
            workbook.getSheetAt(0).setColumnWidth(cell.getColumnIndex(), 10240);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
        }
        //设置表头样式（excel第二行，动态列名称）
        if (relativeRowIndex==1){
            for (int i = 0; i < names.size(); i++) {
                if (cell.getColumnIndex() == i){
                    CellStyle cellStyle = workbook.createCellStyle();
                    //设置 水平居中
                    cellStyle.setAlignment(HorizontalAlignment.LEFT);
                    //设置 垂直居中
                    cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
                    Font font = workbook.createFont();
                    //颜色
                    font.setColor(IndexedColors.BLUE.getIndex());
                    //加粗
                    font.setBold(true);
                    font.setFontName("宋体");
                    font.setFontHeightInPoints((short) 12);
                    cellStyle.setFont(font);
                    cell.setCellStyle(cellStyle);
                }
            }
        }

    }

    @Override
    protected void setContentCellStyle(Cell cell, Head head, Integer relativeRowIndex) {

    }
}
