package com.niulijie.jdk8.stream;

import com.niulijie.jdk8.dto.Employee;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author niuli
 *
 * stream的中间操作--惰性求值
 *
 */
public class StreamAPI1 {
    /**
     * 1.筛选与切片
     * 1.1 filter：接收lambda，从stream中排除某些元素
     * 1.2 limit：截断stream，使其元素不超过给定数量
     * 1.3 skip(n)：跳过元素，返回一个扔掉了前n个元素色stream，若stream中元素不足n个，则返回一个空strea。
     *          与limit(n)互补
     * 1.4 distinct：筛选，通过stream所生成元素的hashCode()和equals()去除重复元素
     *
     * 2.映射：
     * 2.1 map：接收lambda，将元素转换成其他形式或提取信息。
     *      接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素
     * 2.2 flatMap:接收一个函数作为参数，将stream中的每个值都换成另一个stream，然后把所有的stream连接成一个stream
     *
     * 3.排序
     * 3.1 sorted() 自然排序(Comparable)--compareTo
     * 3.2 sorted(Comparator com) 定制排序(Comparator)
     */
    public static void main(String[] args) {

        /**
         * 数组转集合
         */
        List<Employee> employees = Arrays.asList(
                new Employee("张三",18,9999.99),
                new Employee("李四",28,5555.55),
                new Employee("王五",38,6666.66),
                new Employee("赵六",50,2222.22),
                new Employee("田七",8,3333.33),
                new Employee("田七",8,3333.33),
                new Employee("田七",8,3333.33)
        );

        System.out.println("--------------1.1 filter---------------------------------------------------");
        /**
         * filter
         */
        //内部迭代：迭代操作由Stream API 完成
        employees.stream().filter(employee -> employee.getAge() >= 35).forEach(System.out::println);
        employees.stream()
                .filter(employee -> {
                    //有多少数据出现多少次
                    System.out.println("过滤操作");
                    return employee.getAge() >= 35;
                })
                .forEach(System.out::println);

        //外部迭代
        Iterator<Employee> iterator = employees.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }

        System.out.println("--------------1.2 limit---------------------------------------------------");
        /**
         * limit
         */
        employees.stream().filter(employee -> employee.getSalary() > 5000).limit(2).forEach(System.out::println);
        employees.stream()
                .filter(employee -> {
                    //短路操作，只要满足条件就截断 && ||
                    System.out.println("短路");
                    return employee.getSalary() > 5000;
                })
                .limit(2)
                .forEach(System.out::println);

        System.out.println("--------------1.3 skip---------------------------------------------------");
        /**
         * skip
         */
        employees.stream().filter(employee -> employee.getAge() >= 35).skip(2).forEach(System.out::println);

        System.out.println("--------------1.4 distinct---------------------------------------------------");
        /**
         * distinct 重写了hashCode和equals方法后是3条数据，不重新是5条数据
         */
        employees.stream().filter(employee -> employee.getAge() <= 35).distinct().forEach(System.out::println);

        System.out.println("--------------2.1 map---------------------------------------------------");
        /**
         * map
         */
        List<String> list = Arrays.asList("ccc", "aaa", "bbb", "ddd", "eee");
        //将元素转换成其他形式
        System.out.println("--------------2.1.1 map将元素转换成其他形式---------------------------------------------------");
        list.stream().map(str -> str.toUpperCase()).forEach(System.out::println);
        System.out.println("--------------2.1.2 map提取信息---------------------------------------------------");
        employees.stream().map(Employee::getName).forEach(System.out::println);
        System.out.println("--------------2.1.3 map完成flatMap功能---------------------------------------------------");
        Stream<Stream<Character>> streamStream = list.stream().map(StreamAPI1::filterCharacter);
        streamStream.forEach(characterStream -> characterStream.forEach(System.out::println));
        System.out.println("--------------2.2 flatMap---------------------------------------------------");
        /**
         * flatMap
         */
        list.stream().flatMap(StreamAPI1::filterCharacter).forEach(System.out::println);

        System.out.println("--------------3.1 sorted() 自然排序(Comparable)--compareTo---------------------------------------------------");
        /**
         * sorted()
         */
        list.stream().sorted().forEach(System.out::println);

        System.out.println("--------------3.2 sorted(Comparator com) 定制排序(Comparator)---------------------------------------------------");
        /**
         * 3.2 sorted(Comparator com) 定制排序
         */
        employees.stream()
                .sorted((e1,e2) -> {
                    if(e1.getAge().compareTo(e2.getAge()) == 0){
                        return e1.getName().compareTo(e2.getName());
                    }else {
                        return e1.getAge().compareTo(e2.getAge());
                    }
        }).forEach(System.out::println);
    }

    public static Stream<Character> filterCharacter(String str){
        List<Character> list = new ArrayList<>();
        for (Character c : str.toCharArray()) {
            list.add(c);
        }
        return list.stream();
    }
}
