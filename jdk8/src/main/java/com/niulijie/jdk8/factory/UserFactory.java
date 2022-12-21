package com.niulijie.jdk8.factory;

import com.niulijie.jdk8.annotation.Init;
import com.niulijie.jdk8.dto.AnnotationUser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class UserFactory {

    public static AnnotationUser create() {
        AnnotationUser user = new AnnotationUser();


        // 获取User类中所有的方法（getDeclaredMethods也行）
        Method[] methods = AnnotationUser.class.getMethods();


        try
        {
            for (Method method : methods)
            {
                // 如果一个注解指定注解类型是存在于此元素上此方法返回true，否则返回false
                //参数 -- 对应于注解类型的Class对象
                if (method.isAnnotationPresent(Init.class)) {
                    //此方法返回该元素的注解在此元素的指定注释类型（如果存在），否则返回null
                    Init init = method.getAnnotation(Init.class);
                    // 如果Method代表了一个方法 那么调用它的invoke就相当于执行了它代表的这个方法,在这里就是给set方法赋值
                    //method.invoke(user, init.name());
                    method.invoke(user, init.age());
                    //method.invoke(user, init.schools());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }


        return user;
    }

    public static AnnotationUser create2() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        // 通过反射获取类的Class对象：一个类在内存中只有一个class对象；一个类被加载后，类的整个结构都会被封装在Class对象中
        Class<?> annotationUser = Class.forName("com.niulijie.jdk8.dto.AnnotationUser");
        // 获取实例化对象
        AnnotationUser annotationUserTemp = (AnnotationUser) annotationUser.newInstance();
        // 获取User类中所有的方法（getDeclaredMethods也行）
        Method[] methods = annotationUser.getMethods();

        // 定义在类上有值，定义在属性上没有
        Init annotation = annotationUser.getAnnotation(Init.class);
        System.out.println(annotation);
        try {
            for (Method method : methods) {
                // 如果一个注解指定注解类型是存在于此元素上此方法返回true，否则返回false
                //参数 -- 对应于注解类型的Class对象
                if (method.isAnnotationPresent(Init.class)) {
                    //此方法返回该元素的注解在此元素的指定注释类型（如果存在），否则返回null
                    Init init = method.getAnnotation(Init.class);
                    // 如果Method代表了一个方法 那么调用它的invoke就相当于执行了它代表的这个方法,在这里就是给set方法赋值
                    //method.invoke(user, init.name());
                    method.invoke(annotationUserTemp, init.age());
                    //method.invoke(user, init.schools());
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return annotationUserTemp;
    }
}
