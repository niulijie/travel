package com.niulijie.spring.component.config;


import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * 实现TypeFilter接口—自定义规则
 * @author niulijie
 */
public class MyFilterType implements TypeFilter {
    /**
     * 确定此筛选器是否与给定的所描述的元数据类匹配。
     *
     * @param metadataReader        the metadata reader for the target class 目标类的元数据读取器
     * @param metadataReaderFactory a factory for obtaining metadata readers
     *                              for other classes (such as superclasses and interfaces)
     *                              为其他类(如超类和接口)获取元数据读取器的工厂
     * @return whether this filter matches
     * @throws IOException in case of I/O failure when reading metadata
     */
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        //获取当前类注解的信息
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        //获取当前正在扫描的类信息
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        //获取当前类资源(类的路径)
        Resource resource = metadataReader.getResource();
        String className = classMetadata.getClassName();
        System.out.println("-----------------className:"+className);
        //当类包含Controller字符, 则匹配成功,返回true
        //excludeFilters返回true---->会被过滤掉
        //includeFilters返回true---->会通过
        if (className.contains("Order")) {
            return true;
        }
        return false;
    }
}
