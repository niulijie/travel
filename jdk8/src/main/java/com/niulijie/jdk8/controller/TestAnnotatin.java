package com.niulijie.jdk8.controller;

import com.niulijie.jdk8.dto.AnnotationUser;
import com.niulijie.jdk8.factory.UserFactory;

public class TestAnnotatin {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        //AnnotationUser annotationUser = new AnnotationUser();
        AnnotationUser annotationUser = UserFactory.create2();
        System.out.println(annotationUser.getAge());
        annotationUser.setAge(10);
        System.out.println(annotationUser.getAge());
    }
}
