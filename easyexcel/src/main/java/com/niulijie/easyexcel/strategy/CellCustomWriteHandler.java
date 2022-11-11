package com.niulijie.easyexcel.strategy;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.niulijie.easyexcel.enums.Rule;
import com.niulijie.easyexcel.mapper.UcAttrDictMapper;
import com.niulijie.easyexcel.pojo.entity.UcAttrDict;
import com.niulijie.easyexcel.pojo.param.HeadAttrSimple;
import com.niulijie.easyexcel.utils.SpringContextUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.compress.utils.Sets;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 动态下拉框设置
 * @author niuli
 */
public class CellCustomWriteHandler implements SheetWriteHandler {

    /**
     * 动态表头的数据集合
     */
    private List<HeadAttrSimple> attrs;

    /**
     * 自定义属性字段
     */
    Map<String, List<String>> ucAttrDictMap;

    /**
     * 下拉框创建隐藏sheet序号
     */
    private int count = 1;

    /**
     * 生成多选选项结构栈
     */
    private Stack<String> stack = new Stack();

    public CellCustomWriteHandler(List<HeadAttrSimple> attrs) {
        if(!CollectionUtils.isEmpty(attrs)){
            //定义下拉框中的数据 并添加到map中
            UcAttrDictMapper ucAttrDictMapper = SpringContextUtils.getBean(UcAttrDictMapper.class);
            List<UcAttrDict> ucAttrDictList = ucAttrDictMapper.selectList(Wrappers.<UcAttrDict>lambdaQuery()
                    .eq(UcAttrDict::getStatus, 0));
            Map<String, List<String>> ucAttrDictMap = ucAttrDictList.stream()
                    .collect(Collectors.groupingBy(UcAttrDict::getAttrId, Collectors.mapping(UcAttrDict::getAttrValue, Collectors.toList())));
            this.ucAttrDictMap = ucAttrDictMap;
        }
        this.attrs = attrs;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        // 需要设置下拉框的sheet页
        DataValidationHelper helper = writeSheetHolder.getSheet().getDataValidationHelper();

        // 遍历动态表头，根据表头属性动态设置单元格格式
        for (int i = 0; i < attrs.size(); i++) {
            HeadAttrSimple headAttrSimple = attrs.get(i);
            // 根据属性类型判断展示方式
            Integer attrType = headAttrSimple.getAttrType();
            // 需要设置下拉的属性集合：单选，下拉，开关; 多选 -> 暂时当作单选处理
            HashSet<Integer> mapDown = Sets.newHashSet(Rule.RADIO.getAttrId(), Rule.SELECT.getAttrId(), Rule.SWITCH.getAttrId(), Rule.CHECKBOX.getAttrId());
            // 需要设置下拉
            if (mapDown.contains(attrType)) {
                List<String> ucAttrDictList = Lists.newArrayList();
                // 开关
                if(Objects.equals(attrType, Rule.SWITCH.getAttrId())){
                    // 自定义好了的
                    if(headAttrSimple.getAttrField().equals("status")){
                        ucAttrDictList.add("启用");
                        ucAttrDictList.add("停用");
                    }else {
                        ucAttrDictList.add("开启");
                        ucAttrDictList.add("关闭");
                    }
                    // 多选
                } else if (Objects.equals(attrType, Rule.CHECKBOX.getAttrId())){
                    List<String> sourceList = ucAttrDictMap.get(headAttrSimple.getAttrId()).stream().collect(Collectors.toList());
                    ucAttrDictList.addAll(getDictList(sourceList));
                } else {
                    ucAttrDictList.addAll(ucAttrDictMap.get(headAttrSimple.getAttrId()).stream().collect(Collectors.toList()));
                }
                generateDropDown(writeWorkbookHolder, writeSheetHolder, helper, i, ucAttrDictList);
            }

            //需要设置为文本格式
            generateTextType(writeSheetHolder, i);
        }
    }

    private List<String> getDictList(List<String> sourceList) {
        List<String> resultList = new ArrayList<>();
        for (int j = 0; j <= sourceList.size(); j++) {
            randomCombination(sourceList, j, 0, 0, resultList);
        }
        return resultList;
    }

    public List<String> randomCombination(List<String> sourceList, int targ, int has, int cur, List<String> list) {
        if (has == targ) {
            if (!stack.isEmpty()) {
                list.add(StrUtil.join("+", stack));
            }
            return null;
        }
        for (int i = cur; i < sourceList.size(); i++) {
            if (!stack.contains(sourceList.get(i))) {
                stack.add(sourceList.get(i));
                randomCombination(sourceList, targ, has + 1, i, list);
                stack.pop();//将上一组组合清除
            }
        }
        return list;
    }

    /**
     * 身份证号整列需要设置为文本格式
     * @param writeSheetHolder
     * @param index
     */
    private void generateTextType(WriteSheetHolder writeSheetHolder, int index) {
        Sheet sheet = writeSheetHolder.getSheet();
        Workbook workbook = sheet.getWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        DataFormat dataFormat = workbook.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("@"));
        sheet.setDefaultColumnStyle(index, cellStyle);
    }

    /**
     * 设置下拉框值 -不创建sheet页
     * @param writeWorkbookHolder
     * @param writeSheetHolder
     * @param helper
     * @param index
     * @param ucAttrDictList
     */
    private void generateDropDown2(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder, DataValidationHelper helper, int index, List<String> ucAttrDictList) {
        Sheet sheet = writeSheetHolder.getSheet();

        // 设置下拉列表的行： 首行，末行，首列，末列
        CellRangeAddressList rangeList = new CellRangeAddressList(2, 65535, index, index);
        // 设置下拉列表的值
        DataValidationConstraint constraint = helper.createExplicitListConstraint(ucAttrDictList.toArray(new String[]{}));
        // 设置约束
        DataValidation validation = helper.createValidation(constraint, rangeList);
        // 阻止输入非下拉选项的值
        validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        validation.setShowErrorBox(true);
        validation.setSuppressDropDownArrow(true);
        validation.createErrorBox("提示", "请输入下拉选项中的内容");
        sheet.addValidationData(validation);
    }

    /**
     * 设置下拉框值 - 创建sheet，并且赋值（为了防止下拉框的行数与隐藏域的行数相对应，将隐藏域加到结束行之后）
     * @param writeWorkbookHolder
     * @param writeSheetHolder
     * @param helper
     * @param index 表示对应的excel字段的序号
     * @param ucAttrDictList 下拉框内容
     * @description 可以解决单个单元格内容只能写入255长度问题
     */
    private void generateDropDown(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder, DataValidationHelper helper, int index, List<String> ucAttrDictList) {
        //获取工作簿
        // 创建sheet，突破下拉框255的限制
        //获取一个workbook
        Workbook workbook = writeWorkbookHolder.getWorkbook();
        //定义数据字典sheet的名称
        String sheetName = "hidden" + index;
        //1.创建一个隐藏的sheet 名称为 hidden + 下拉列序号
        Sheet providerSheet = workbook.createSheet(sheetName);
        // 设置隐藏
        workbook.setSheetHidden(count, true);
        // 设置下拉列表的行： 首行，末行，首列，末列
        CellRangeAddressList addressList = new CellRangeAddressList(2, 65535, index, index);

        String[] values = ucAttrDictList.toArray(new String[]{});
        // 设置字典sheet页的值 每一列一个字典项
        for (int i = 0, length = values.length; i < length; i++) {
            // i:表示你开始的行数  0表示你开始的列数
            Row row = providerSheet.getRow(i);
            if (row == null) {
                row = providerSheet.createRow(i);
            }
            row.createCell(index).setCellValue(values[i]);
        }
        //4 $A$1:$A$N代表 以A列1行开始获取N行下拉数据
        String excelLine = getExcelLine(index);
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
        dataValidation.createErrorBox("提示", "请输入下拉选项中的内容！");
        // 添加下拉框约束
        writeSheetHolder.getSheet().addValidationData(dataValidation);
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
