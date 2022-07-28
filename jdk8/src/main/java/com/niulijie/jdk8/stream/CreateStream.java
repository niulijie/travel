package com.niulijie.jdk8.stream;


import com.niulijie.jdk8.dto.Customer;
import com.niulijie.jdk8.dto.Employee;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author niuli
 * 创建Stream有四种方式
 */
public class CreateStream {

    public static void main(String[] args) {

        /**
         * 1. 可以通过 Collection 系列集合提供的stream() 或 papallelStream()
         */
        List<String> list = new ArrayList<>();
        Stream<String> stream = list.stream();

        /**
         * 2. 通过Arrays 中的静态方法stream() 获取数组流
         */
        Employee[] employees = new Employee[10];
        Arrays.stream(employees);

        /**
         * 3. 通过Stream 类中的静态 of()
         */
        Stream<String> stringStream = Stream.of("aa", "bb", "dd");

        /**
         * 4. 创建无限流
         */
        //迭代
        Stream<Integer> integerStream = Stream.iterate(0, x -> x + 2);
        integerStream.forEach(System.out::println);

        //生成
        Stream.generate(() -> Math.random()).forEach(System.out::println);

        /**
         * 5. contract,两个流合并成一个流
         */
        Stream<String> streamA = Stream.of("张无忌","张翠山");
        Stream<String> streamB = Stream.of("美羊羊","喜羊羊");
        //写法1
        Stream.concat(streamA, streamB).forEach(System.out::println);
        //写法2
        Stream<String> result=  Stream.concat(streamA, streamB);
        result.forEach(name-> System.out.println(name));

        Object[] objects = Stream.of(1, 2, 3, 4, 5, 6, 7, 8).filter(integer -> integer % 2 == 0).filter(integer -> integer > 3).toArray();
        System.out.println(Arrays.asList(objects));

        List<Optional<Customer>> customers = Arrays.asList(
                Optional.of(new Customer("日拱一兵", 18)),
                Optional.of(new Customer("卑微的小开发", 22)),
                Optional.empty(),
                Optional.of(new Customer("OOT", 21)),
                Optional.empty(),
                Optional.of(new Customer("温柔一刀", 23)),
                Optional.empty()
        );

        long numberOf65PlusCustomers = customers
                .stream()
                .flatMap(c -> c
                        .map(Stream::of)
                        .orElseGet(Stream::empty))
                .filter(c -> c.getAge() > 18)
                .count();

        System.out.println(numberOf65PlusCustomers);
    }
}
