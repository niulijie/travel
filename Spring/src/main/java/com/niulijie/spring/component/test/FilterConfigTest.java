package com.niulijie.spring.component.test;

import com.niulijie.spring.component.config.MyFilterType;
import com.niulijie.spring.component.controller.OrderRepository;
import com.niulijie.spring.component.controller.SellComponent;
import com.niulijie.spring.component.controller.UserService2;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

/**
 @ComponentScan(value="com.niulijie.spring.component.controller",useDefaultFilters=false,
 excludeFilters={
 @ComponentScan.Filter(type= FilterType.ANNOTATION,classes={Controller.class}),
 @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE,classes={OrderRepository.class, SellComponent.class}),
 @ComponentScan.Filter(type = FilterType.CUSTOM,classes = {MyFilterType.class})})
 展示filterConfigTest
 =======================================================================================
 @ComponentScan(value="com.niulijie.spring.component.controller",useDefaultFilters=true,(或者无此条件)
 excludeFilters={
 @ComponentScan.Filter(type= FilterType.ANNOTATION,classes={Controller.class}),
 @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE,classes={OrderRepository.class, SellComponent.class}),
 @ComponentScan.Filter(type = FilterType.CUSTOM,classes = {MyFilterType.class})})
 展示
 filterConfigTest
 userService2
 * @author edz
 */
@ComponentScan(value="com.niulijie.spring.component.controller",useDefaultFilters=false,
        excludeFilters={
                @ComponentScan.Filter(type= FilterType.ANNOTATION,classes={Controller.class}),
                @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE,classes={OrderRepository.class, SellComponent.class}),
                @ComponentScan.Filter(type = FilterType.CUSTOM,classes = {MyFilterType.class})})
//@ComponentScan(value="com.niulijie.spring.component.controller",useDefaultFilters=false,
//        includeFilters={
//                @ComponentScan.Filter(type= FilterType.ANNOTATION,classes={Controller.class}),
//                @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE,classes={OrderRepository.class, SellComponent.class}),
//                @ComponentScan.Filter(type = FilterType.CUSTOM,classes = {MyFilterType.class})})
//@ComponentScan(value="com.niulijie.spring.component.controller",useDefaultFilters=false,
//        includeFilters={
//                @ComponentScan.Filter(type= FilterType.ANNOTATION,classes={Controller.class}),
//                @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE,classes={UserService2.class})})
@Configuration
public class FilterConfigTest {
}
