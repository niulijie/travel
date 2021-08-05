package com.niulijie.jdk8.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * java8 内置四大核心函数式接口
 *
 * Consumer<T> : 消费型接口
 *   void accept(T t);
 *
 * Supplier<T> : 供给型接口
 *   T get();
 *
 * Function<T,R> : 函数型接口
 *   R apply(T t);
 *
 * Predicate<T>  : 断言型接口
 *   boolean test(T t);
 */
public class LambdaInterface {

    /**
     * Consumer<T> : 消费型接口
     * @param money
     * @param con
     */
    public static void happy(double money, Consumer<Double> con){
        con.accept(money);
    }

    /**
     * Supplier<T> : 供给型接口
     * @param num
     * @param supplier
     * @return
     */
    public static List<Integer> getNumList(int num, Supplier<Integer> supplier){
        List<Integer> numList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Integer integer = supplier.get();
            numList.add(integer);
        }
        return numList;
    }

    /**
     * Function<T,R> : 函数型接口
     * @param str
     * @param function
     * @return
     */
    public static String handlerStr(String str, Function<String,String> function){
        return function.apply(str);
    }

    /**
     * Predicate<T>  : 断言型接口
     * @param stringList
     * @param predicate
     * @return
     */
    public static List<String> filterStr(List<String> stringList, Predicate<String> predicate){
        List<String> strList = new ArrayList<>();
        for (String str : stringList) {
            if(predicate.test(str)){
                strList.add(str);
            }
        }
        return strList;
    }
    public static void main(String[] args) {
        //happy(10000,(m) -> System.out.println("每次消费" + m +"元"));

//        List<Integer> numList = getNumList(10, () -> (int)(Math.random() * 100));
//        System.out.println(numList);

//        String handlerStr = handlerStr("\t\t\t   你是猪吗?   ", str -> str.trim());
//        System.out.println(handlerStr);

        List<String> strings = Arrays.asList("Hello", "How", "What", "Why", "Who");
        List<String> list = filterStr(strings, str -> str.length() > 3);
        System.out.println(list);
    }
}
