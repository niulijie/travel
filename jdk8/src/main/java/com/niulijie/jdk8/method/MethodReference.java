package com.niulijie.jdk8.method;

import com.niulijie.jdk8.dto.Employee;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 一、方法引用：若lambda 体中的内容有方法已经实现了，我们可以使用“方法引用”
 *          可以理解伟方法引入是lambda 表达式的另外一种表现形式
 *
 * 主要有三种语法格式：
 *
 * 对象::实例方法名
 *
 * 类::静态方法名
 *
 * 类::实例方法名
 *
 * 注意：
 * 1.lambda体中方法的参数列表和返回值类型，要与函数式接口中抽象方法中的参数列表和返回值一致 -> 方法引用
 * 2.若lambda参数列表中，参数1是实例方法的调用者，参数2是实例方法的参数值，可以使用 类::实例方法名
 *
 * 二、构造器引用
 * 格式：
 * ClassName::new
 * 注意：需要调用的构造器参数列表需要与函数式接口中抽象方法的参数列表保持一致
 *
 * 三、数组引用
 * Type::new
 */
public class MethodReference {

    public static void main(String[] args) {

        System.out.println("-------------------------对象::实例方法名----------------------------------------");
        /**
         * 对象::实例方法名
         */
        Consumer<String> consumer = s -> System.out.println(s);
        consumer.accept("Hello World");

        PrintStream printStream = System.out;
        Consumer<String> consumer1 = s -> printStream.println(s);
        consumer1.accept("I love you");
        //对象::实例方法名 lambda方法体中的方法已经有方法实现的时候，可以用方法引用来实现
        //注意前提：抽象方法中的参数列表和lambda体中方法的参数列表一致，两者返回值类型也一致 -> 方法引用
        Consumer<String> consumer2 = printStream::println;
        consumer2.accept("I love life");

        Consumer<String> consumer3 = System.out::println;
        consumer3.accept("I love louqun");

        System.out.println("-----------------------------------------------------------------");

        Employee employee = new Employee("张三",18,999.78);
        Supplier<String> supplier = () -> employee.getName();
        String str = supplier.get();
        System.out.println(str);

        Supplier<Integer> supplier1 = () -> employee.getAge();
        Integer integer = supplier1.get();
        System.out.println(integer);

        System.out.println("----------------类::静态方法名-------------------------------------------------");

        /**
         * 类::静态方法名
         */
        Comparator<Integer> comparator = (x,y) -> Integer.compare(x,y);
        int compare = comparator.compare(8, 9);
        System.out.println(compare);

        Comparator<Integer> comparator1 = Integer::compare;
        int compare1 = comparator1.compare(12, 9);
        System.out.println(compare1);

        System.out.println("--------------类::实例方法名---------------------------------------------------");

        /**
         *  类::实例方法名 参数1是实例方法的调用者，参数2是实例方法的参数值
         */
        BiPredicate<String,String> flag = (x,y) -> x.equals(y);
        boolean test = flag.test("ab", "cd");
        System.out.println(test);

        BiPredicate<String,String> flag1 = String::equals;
        boolean test1 = flag1.test("ab", "ab");
        System.out.println(test1);

        System.out.println("--------------构造器引用---------------------------------------------------");
        /**
         * 构造器引用
         */
        Supplier<Employee> employeeSupplier = () -> new Employee();
        Employee employee1 = employeeSupplier.get();
        System.out.println(employee1);

        Supplier<Employee> employeeSupplier1 = Employee::new;
        Employee employee2 = employeeSupplier1.get();
        System.out.println(employee2);


        System.out.println("--------------数组引用---------------------------------------------------");
        /**
         * 数组引用
         */
        Function<Integer,String[]> fun = (x) -> new String[x];
        String[] apply = fun.apply(10);
        System.out.println(apply.length);

        Function<Integer,String[]> fun1 = String[]::new;
        String[] apply1 = fun1.apply(20);
        System.out.println(apply1.length);

        Function<Integer, List<String>> fun2 = ArrayList<String>::new;
        String[] apply2 = fun1.apply(30);
        System.out.println(apply2.length);


    }
}
