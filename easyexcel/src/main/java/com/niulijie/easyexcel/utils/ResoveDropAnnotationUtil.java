package com.niulijie.easyexcel.utils;

import com.niulijie.easyexcel.annotation.DropDownSetField;
import com.niulijie.easyexcel.service.DropDownSetInterface;

import java.util.Arrays;
import java.util.Optional;

/**
 * 解析下拉数据集工具
 *
 * 获得数据源
 * @author niuli
 */
public class ResoveDropAnnotationUtil {

    // 填充年级下拉框
    public static String[] dynamicListResove(DropDownSetField dropDownSetField, String fieldName) {

        if (!Optional.ofNullable(dropDownSetField).isPresent()) {
            return null;
        }

        // 获取固定下拉信息
        String[] source = dropDownSetField.source();
        if (source.length > 0) {
            return source;
        }

        // 获取动态的下拉数据
        Class<? extends DropDownSetInterface>[] classes = dropDownSetField.sourceClass();
        if (classes.length > 0) {
            try {
                DropDownSetInterface dropDownSetInterface = Arrays.stream(classes).findFirst().get().newInstance();

                // 获得数据源，可根据filedName，判断并调用对应的方法，填充数据源
                String[] dynamicSource = null;
                if ("gradeList".equals(fieldName)) {
                    dynamicSource = dropDownSetInterface.getGradeListSource();
                }

                if (null != dynamicSource && dynamicSource.length > 0) {
                    return dynamicSource;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

