package com.niulijie.easyexcel.strategy;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CustomSheetWriteHandler 是 CustomCellWriteHandler 备份
 * @author niuli
 */
public class CustomSheetWriteHandler implements SheetWriteHandler {
    /**
     * 动态下拉框的数据
     */
    private List<String> list;

    private Map<Integer, List<String>> selectMap;

    /*public CustomSheetWriteHandler(List<String> list) {
        this.list= list;
    }*/

    /*public CustomSheetWriteHandler(List<Integer> columnIndexes, List<HeadAttrSimple> attrs) {
        this.list= attrs;
        this.columnIndexes = columnIndexes;
    }*/

    public CustomSheetWriteHandler() {

    }

    public CustomSheetWriteHandler(Map<Integer, List<String>> selectMap) {
        this.selectMap = selectMap;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        // 需要设置下拉框的sheet页
        DataValidationHelper helper = writeSheetHolder.getSheet().getDataValidationHelper();
        Map<Integer, String[]> mapDropDown = new HashMap<>();
        /*String[] listArray= new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            listArray[i] = list.get(i);
        }*/

        //定义下拉框中的数据 并添加到map中 这里的key是写死的
        //第一个数表示对应的excel字段的序号，第二个则是数组
        //mapDropDown.put(9, listArray);
        mapDropDown.put(3, new String[]{"下拉字段1", "下拉字段2", "下拉字段3"});
        mapDropDown.put(4, new String[]{"男", "女"});
        //获取工作簿
        // 创建sheet，突破下拉框255的限制
        //获取一个workbook
        Workbook workbook = writeWorkbookHolder.getWorkbook();

        // 设置隐藏
        //workbook.setSheetHidden(1,true);
        //2.循环赋值（为了防止下拉框的行数与隐藏域的行数相对应，将隐藏域加到结束行之后）
        for (Map.Entry<Integer, String[]> entry : mapDropDown.entrySet()) {
            //定义数据字典sheet的名称
            String sheetName = "hidden"+ entry.getKey();
            //1.创建一个隐藏的sheet 名称为 providerSheet
            Sheet providerSheet = workbook.createSheet(sheetName);
            CellRangeAddressList addressList = new CellRangeAddressList(2, 65535, entry.getKey(), entry.getKey());
            String[] values = entry.getValue();
            // 设置字典sheet页的值 每一列一个字典项
            for (int i = 0, length = values.length; i < length; i++) {
                // i:表示你开始的行数  0表示你开始的列数
                Row row = providerSheet.getRow(i);
                if (row == null) {
                    row = providerSheet.createRow(i);
                }
                row.createCell(entry.getKey()).setCellValue(values[i]);
            }
            //4 $A$1:$A$N代表 以A列1行开始获取N行下拉数据
            String excelLine = getExcelLine(entry.getKey());
            String refers = "=" + sheetName + "!$" + excelLine + "$1:$" + excelLine + "$" + (values.length);
            //5 将刚才设置的sheet引用到你的下拉列表中
            DataValidationConstraint constraint = helper.createFormulaListConstraint(refers);
            // 设置约束
            DataValidation dataValidation = helper.createValidation(constraint, addressList);
            if (dataValidation instanceof HSSFDataValidation) {
                dataValidation.setSuppressDropDownArrow(false);
            } else {
                dataValidation.setSuppressDropDownArrow(true);
                dataValidation.setShowErrorBox(true);
            }
            // 阻止输入非下拉框的值
            dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            dataValidation.createErrorBox("提示", "此值与单元格定义格式不一致！");
            // 添加下拉框约束
            writeSheetHolder.getSheet().addValidationData(dataValidation);
        }
    }

    /**
     * @param num 列数
     * @return java.lang.String
     * @Description 返回excel列标A-Z-AA-ZZ
     */
    public static String getExcelLine(int num) {
        String line = "";
        int first = num / 26;
        int second = num % 26;
        if (first > 0) {
            line = (char) ('A' + first - 1) + "";
        }
        line += (char) ('A' + second) + "";
        return line;
    }
}

