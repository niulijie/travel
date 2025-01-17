package com.niulijie.easyexcel.utils;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: sharing-backstage
 * @Description: 处理下拉
 * @Author: zwx
 * @Date: 2022/3/3 15:16
 */
public class SelectSheetWriteHandler implements SheetWriteHandler {
    private Map<Integer, List<String>> selectMap;

    private List<Map<Integer, List<String>>> selectMapList;

    private char[] alphabet = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public SelectSheetWriteHandler(Map<Integer, List<String>> selectMap) {
        this.selectMap = selectMap;
    }

    public SelectSheetWriteHandler(List<Map<Integer, List<String>>> selectMapList) {
        this.selectMapList = selectMapList;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        if (selectMapList == null || selectMapList.size() == 0) {
            return;
        }
        Map<Integer, List<String>> mapDropDown = new HashMap<>();
        mapDropDown.put(3, Arrays.asList("下拉字段1", "下拉字段2", "下拉字段3"));
        Map<Integer, List<String>> mapDropDown2 = new HashMap<>();
        mapDropDown2.put(4, Arrays.asList("男", "女"));
        selectMapList.add(mapDropDown);
        selectMapList.add(mapDropDown2);
        DataValidationHelper helper = writeSheetHolder.getSheet().getDataValidationHelper();
        for (Map<Integer, List<String>> integerListMap : selectMapList) {
            // 需要设置下拉框的sheet页
            String dictSheetName = "字典sheet";
            Workbook workbook = writeWorkbookHolder.getWorkbook();
            // 数据字典的sheet页
            Sheet dictSheet = workbook.createSheet(dictSheetName);
            for (Map.Entry<Integer, List<String>> entry : integerListMap.entrySet()) {
                // 设置下拉单元格的首行、末行、首列、末列
                CellRangeAddressList rangeAddressList = new CellRangeAddressList(2, 65533, entry.getKey(), entry.getKey());
                int rowLen = entry.getValue().size();
                // 设置字典sheet页的值 每一列一个字典项
                for (int i = 0; i < rowLen; i++) {
                    Row row = dictSheet.getRow(i);
                    if (row == null) {
                        row = dictSheet.createRow(i);
                    }
                    row.createCell(entry.getKey()).setCellValue(entry.getValue().get(i));
                }
                String excelColumn = getExcelColumn(entry.getKey());
                // 下拉框数据来源 eg:字典sheet!$B1:$B2
                String refers = dictSheetName + "!$" + excelColumn + "$1:$" + excelColumn + "$" + rowLen;
                // 创建可被其他单元格引用的名称
                Name name = workbook.createName();
                // 设置名称的名字
                name.setNameName("dict" + entry.getKey());
                // 设置公式
                name.setRefersToFormula(refers);
                // 设置引用约束 - 设置下拉列表的值
                DataValidationConstraint constraint = helper.createFormulaListConstraint("dict" + entry.getKey());
                // 设置约束
                DataValidation validation = helper.createValidation(constraint, rangeAddressList);
                if (validation instanceof HSSFDataValidation) {
                    validation.setSuppressDropDownArrow(false);
                } else {
                    validation.setSuppressDropDownArrow(true);
                    validation.setShowErrorBox(true);
                }
                // 阻止输入非下拉框的值
                validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
                validation.createErrorBox("提示", "此值与单元格定义格式不一致！");
                // 添加下拉框约束
                writeSheetHolder.getSheet().addValidationData(validation);
            }
        }

    }

    /**
     * 将数字列转化成为字母列
     *
     * @param num
     * @return
     */
    private String getExcelColumn(int num) {
        String column = "";
        int len = alphabet.length - 1;
        int first = num / len;
        int second = num % len;
        if (num <= len) {
            column = alphabet[num] + "";
        } else {
            column = alphabet[first - 1] + "";
            if (second == 0) {
                column = column + alphabet[len] + "";
            } else {
                column = column + alphabet[second - 1] + "";
            }
        }
        return column;
    }
}

