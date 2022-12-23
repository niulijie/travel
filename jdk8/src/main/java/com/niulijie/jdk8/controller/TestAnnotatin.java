package com.niulijie.jdk8.controller;

import com.niulijie.jdk8.annotation.Person;
import com.niulijie.jdk8.annotation.Persons;
import com.niulijie.jdk8.dto.AnnotationNameOnMethod;
import com.niulijie.jdk8.dto.AnnotationPersonOnClass;
import com.niulijie.jdk8.dto.AnnotationUser;
import com.niulijie.jdk8.factory.UserFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class TestAnnotatin {

    public static void main(String[] args){

        // 测试自定义注解@Init
        /*try {
            testAnnotationUser();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }*/

        // 测试可重复使用自定义注解@Name在方法上
        //testAnnotationName();

        // 测试可重复使用自定义注解@Person在类上
        testAnnotationPersonOnClass();
    }

    /**
     * 测试可重复使用自定义注解@Person在类上
     * annotations.length2:1
     * father
     * son
     * husband
     */
    private static void testAnnotationPersonOnClass() {
        Annotation[] annotations = AnnotationPersonOnClass.class.getAnnotations();
        System.out.println("annotations.length2:"+ annotations.length);
        Persons annotation = (Persons) annotations[0];
        for (Person person : annotation.value()) {
            System.out.println(person.role());
        }
    }

    /**
     * 测试可重复使用自定义注解@Name
     * annotations.length:1
     * test:[@com.niulijie.jdk8.annotation.Name$Names(value=[@com.niulijie.jdk8.annotation.Name(name=hello), @com.niulijie.jdk8.annotation.Name(name=world)])]
     */
    private static void testAnnotationName() {
        Method[] methods = AnnotationNameOnMethod.class.getMethods();
        for (Method method : methods) {
            if (Objects.equals(method.getName(), "test")){
                Annotation[] annotations = method.getDeclaredAnnotations();
                System.out.println("annotations.length:"+ annotations.length);
                System.out.println(method.getName() + ":" + Arrays.toString(annotations));
            }
        }
    }

    /**
     * 测试自定义注解@Init
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private static void testAnnotationUser() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        //AnnotationUser annotationUser = new AnnotationUser();
        AnnotationUser annotationUser = UserFactory.create2();
        System.out.println(annotationUser.getAge());
        annotationUser.setAge(10);
        System.out.println(annotationUser.getAge());

    }
}
