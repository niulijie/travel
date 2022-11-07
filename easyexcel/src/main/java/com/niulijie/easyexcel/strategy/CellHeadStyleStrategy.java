package com.niulijie.easyexcel.strategy;

import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.niulijie.easyexcel.pojo.param.HeadAttrSimple;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.List;
import java.util.Objects;

/**
 - 设置表头和填充内容的样式
 * @author niuli
 */
public class CellHeadStyleStrategy extends HorizontalCellStyleStrategy{

    private final WriteCellStyle headWriteCellStyle;
    private final WriteCellStyle contentWriteCellStyle;

    /**
     * 操作列
     */
    private final List<Integer> columnIndexes;

    /**
     * 动态头字段集合
     */
    private List<HeadAttrSimple> headAttrSimples;

    public CellHeadStyleStrategy(List<Integer> columnIndexes, List<HeadAttrSimple> attrs, WriteCellStyle headWriteCellStyle, WriteCellStyle contentWriteCellStyle) {
        this.columnIndexes = columnIndexes;
        this.headWriteCellStyle = headWriteCellStyle;
        this.contentWriteCellStyle = contentWriteCellStyle;
        this.headAttrSimples = attrs;
    }

    /**
     * 设置头样式
     * @param context
     */
    @Override
    protected void setHeadCellStyle(CellWriteHandlerContext context) {
        // 获取字体实例
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontName("宋体");
        // 设置第一列
        if (columnIndexes.get(0).equals(context.getRowIndex())) {
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
            headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            headWriteFont.setFontHeightInPoints((short) 14);
            headWriteFont.setBold(false);
            headWriteFont.setColor(IndexedColors.BLUE.getIndex());
        // 设置第二列
        }else{
            String stringCellValue = context.getCell().getStringCellValue();
            for (HeadAttrSimple headAttrSimple : headAttrSimples) {
                if(stringCellValue.equals(headAttrSimple.getAttrName())){
                    Integer isNull = headAttrSimple.getIsNull();
                    if(Objects.equals(isNull, 0)){
                        headWriteFont.setColor(IndexedColors.BLACK.getIndex());
                    }else {
                        headWriteFont.setColor(IndexedColors.RED.getIndex());
                    }
                }
            }
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            headWriteCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            headWriteFont.setFontHeightInPoints((short) 12);
            headWriteFont.setBold(true);
        }
        headWriteCellStyle.setWriteFont(headWriteFont);
        if (stopProcessing(context)) {
            return;
        }
        WriteCellData<?> cellData = context.getFirstCellData();
        WriteCellStyle.merge(headWriteCellStyle, cellData.getOrCreateStyle());
    }

    /**
     * 设置填充数据样式
     * @param context
     */
    @Override
    protected void setContentCellStyle(CellWriteHandlerContext context) {
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontName("宋体");
        contentWriteFont.setFontHeightInPoints((short) 11);
        //设置数据填充后的实线边框
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 左边框
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        // 上边框
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        // 右边框
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        // 下边框
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        WriteCellData<?> cellData = context.getFirstCellData();
        WriteCellStyle.merge(contentWriteCellStyle, cellData.getOrCreateStyle());
    }

}
