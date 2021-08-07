package com.niulijie.jdk8.stream;

import com.niulijie.jdk8.dto.Employee;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author niuli
 *
 * stream的终止操作
 *
 */
public class StreamAPI2 {

    /**
     * 1.查找与匹配
     * 1.1 allMatch：检查是否匹配所有元素
     * 1.2 anyMatch：检查是否至少匹配一个元素
     * 1.3 monMatch：检查是否没有匹配所有元素
     * 1.4 findFirst：返回第一个元素
     * 1.5 findAny：返回当前stream中的任意元素
     * 1.6 count：返回stream中元素的总个数
     * 1.7 max：返回stream中最大值
     * 1.8 min：返回stream中最小值
     *
     * 2.归约
     * reduce(T identity,BinaryOperator)/reduce(BinaryOperator)
     * 可以将stream中元素反复结合起来，得到一个值
     *
     * 3.收集
     * collect 将stream转换为其他形式，接收一个Collector接口的实现，用于给Stream中元素做汇总的方法
     *
     */
    public static void main(String[] args) {
        /**
         * 数组转集合
         */
        List<Employee> employees = Arrays.asList(
                new Employee("张三",18,9999.99, Employee.Status.FREE),
                new Employee("李四",28,5555.55,Employee.Status.BUSY),
                new Employee("王五",38,6666.66, Employee.Status.VOCATION),
                new Employee("赵六",50,2222.22, Employee.Status.FREE),
                new Employee("田七",8,3333.33, Employee.Status.BUSY),
                new Employee("田七",8,3333.33, Employee.Status.VOCATION),
                new Employee("田七",8,3333.33, Employee.Status.BUSY)
        );

        System.out.println("--------------1.1 allMatch：检查是否匹配所有元素---------------------------------------------------");
        /**
         * allMatch
         */
        boolean b = employees.stream().allMatch(employee -> employee.getStatus().equals(Employee.Status.BUSY));
        System.out.println(b);

        System.out.println("--------------1.2 anyMatch：检查是否至少匹配一个元素---------------------------------------------------");
        /**
         * anyMatch
         */
        boolean b1 = employees.stream().anyMatch(employee -> employee.getStatus().equals(Employee.Status.BUSY));
        System.out.println(b1);

        System.out.println("--------------1.3 monMatch：检查是否没有匹配所有元素---------------------------------------------------");
        /**
         * monMatch
         */
        boolean b3 = employees.stream().noneMatch(employee -> employee.getStatus().equals(Employee.Status.BUSY));
        System.out.println(b3);

        System.out.println("--------------1.4 findFirst：返回第一个元素---------------------------------------------------");
        /**
         * findFirst
         */
        Optional<Employee> optional = employees.stream()
                .sorted((employee1, employee2) -> -Double.compare(employee1.getSalary(), employee2.getSalary()))
                .findFirst();
        //optional.orElse()
        System.out.println(optional.get());

        System.out.println("--------------1.5 findAny：返回当前stream中的任意元素---------------------------------------------------");
        /**
         * findAny
         */
        Optional<Employee> optional1 = employees.stream()
                .filter(employee -> employee.getStatus().equals(Employee.Status.FREE))
                .findAny();
        //optional.orElse()
        System.out.println(optional1.get());

        System.out.println("--------------1.6 count：返回stream中元素的总个数---------------------------------------------------");
        /**
         * count
         */
        long count = employees.stream().count();
        System.out.println(count);

        System.out.println("--------------1.7 max：返回stream中最大值---------------------------------------------------");
        /**
         * max
         */
        Optional<Employee> optional2 = employees.stream()
                .max((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary()));

        System.out.println(optional2.get());

        System.out.println("--------------1.8 min：返回stream中最小值---------------------------------------------------");
        /**
         * min
         */
        Optional<Double> optional3 = employees.stream()
                .map(Employee::getSalary)
                .min(Double::compare);

        System.out.println(optional3.get());

        System.out.println("--------------2 reduce 归约---------------------------------------------------");
        /**
         * reduce
         */
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        Integer sum = list.stream()
                .reduce(0, (x, y) -> x + y);
        System.out.println(sum);

        Optional<Double> reduceSum = employees.stream()
                .map(Employee::getSalary)
                .reduce(Double::sum);
        System.out.println(reduceSum.get());

        System.out.println("--------------3 collect 收集---------------------------------------------------");
        /**
         * collect
         */
        System.out.println("--------------3.1 collect 收集转List---------------------------------------------------");
        List<String> collect = employees.stream()
                .map(Employee::getName)
                .collect(Collectors.toList());
        System.out.println(collect);

        System.out.println("--------------3.2 collect 收集转Set---------------------------------------------------");
        Set<String> set = employees.stream()
                .map(Employee::getName)
                .collect(Collectors.toSet());
        System.out.println(set);

        System.out.println("--------------3.3 collect 收集转其他数据结构---------------------------------------------------");
        HashSet<String> collect1 = employees.stream()
                .map(Employee::getName)
                .collect(Collectors.toCollection(HashSet::new));
        System.out.println(collect1);

        //总数
        System.out.println("--------------3.4 collect 收集总数---------------------------------------------------");
        Long aLong = employees.stream()
                .map(Employee::getName)
                .collect(Collectors.counting());
        System.out.println(aLong);

        //平均值
        System.out.println("--------------3.5 collect 收集平均值---------------------------------------------------");
        Double collect2 = employees.stream()
                .collect(Collectors.averagingDouble(Employee::getSalary));
        System.out.println(collect2);

        //总和
        System.out.println("--------------3.6 collect 收集总和---------------------------------------------------");
        DoubleSummaryStatistics collect3 = employees.stream()
                .collect(Collectors.summarizingDouble(Employee::getSalary));
        System.out.println(collect3);

        //最大值
        System.out.println("--------------3.7 collect 收集最大值 自定义比较---------------------------------------------------");
        Optional<Employee> collect4 = employees.stream()
                .collect(Collectors.maxBy((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary())));
        System.out.println(collect4.get());

        //最小值
        System.out.println("--------------3.8 collect 收集最小值---------------------------------------------------");
        Optional<Double> collect5 = employees.stream()
                .map(Employee::getSalary)
                .collect(Collectors.minBy(Double::compare));
        System.out.println(collect5.get());

        System.out.println("--------------3.9 collect 分组---------------------------------------------------");
        Map<Employee.Status, List<Employee>> collect6 = employees.stream().collect(Collectors.groupingBy(Employee::getStatus));
        System.out.println(collect6);

        System.out.println("--------------3.10 collect 多级分组---------------------------------------------------");
        Map<Employee.Status, Map<String, List<Employee>>> collect7 = employees.stream()
                .collect(Collectors.groupingBy(Employee::getStatus, Collectors.groupingBy(employee -> {
                    if (employee.getAge() <= 35) {
                        return "青年";
                    } else if (employee.getAge() <= 50) {
                        return "中年";
                    } else {
                        return "老年";
                    }
                })));
        System.out.println(collect7);

        System.out.println("--------------3.11 collect 分区---------------------------------------------------");
        Map<Boolean, List<Employee>> collect8 = employees.stream()
                .collect(Collectors.partitioningBy(employee -> employee.getSalary() > 8000));
        System.out.println(collect8);

        System.out.println("--------------3.12 collect 获取组函数---------------------------------------------------");
        DoubleSummaryStatistics collect9 = employees.stream()
                .collect(Collectors.summarizingDouble(Employee::getSalary));
        System.out.println("通过组函数获取最大值"+collect9.getMax());
        System.out.println("通过组函数获取最小值"+collect9.getMin());
        System.out.println("通过组函数获取平均值"+collect9.getAverage());
        System.out.println("通过组函数获取总数"+collect9.getCount());
        System.out.println("通过组函数获取总和"+collect9.getSum());

        System.out.println("--------------3.13 collect 连接1---------------------------------------------------");
        String collect10 = employees.stream()
                .map(Employee::getName)
                .collect(Collectors.joining());
        System.out.println(collect10);

        System.out.println("--------------3.14 collect 连接2---------------------------------------------------");
        String collect11 = employees.stream()
                .map(Employee::getName)
                .collect(Collectors.joining(","));
        System.out.println(collect11);

        System.out.println("--------------3.15 collect 连接3---------------------------------------------------");
        String collect12 = employees.stream()
                .map(Employee::getName)
                .collect(Collectors.joining(",","======","-------"));
        System.out.println(collect12);
    }
}
