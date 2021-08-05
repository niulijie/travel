package com.niulijie.jdk8.lambda;

import com.niulijie.jdk8.dto.Employee;
import com.niulijie.jdk8.dto.FilterEmployeeByAge;
import com.niulijie.jdk8.dto.FilterEmployeeBySalary;
import com.niulijie.jdk8.dto.MyEmployee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestEmployeesLambda {

    /**
     * 数组转集合
     */
    static List<Employee> employees = Arrays.asList(
            new Employee("张三",18,9999.99),
            new Employee("李四",28,5555.55),
            new Employee("王五",38,6666.66),
            new Employee("赵六",50,2222.22),
            new Employee("田七",8,3333.33)
    );

    /**
     * 优化方式一:策略设计模式
     */

    public static List<Employee> filterEmployee(List<Employee> employeeList, MyEmployee<Employee> employee){
        List<Employee> employees = new ArrayList<>();
        employeeList.forEach(
                employeeTemp -> {
                    if(employee.test(employeeTemp)){
                        employees.add(employeeTemp);
                    }
                }
        );
        return employees;
    }

    public static void main(String[] args) {
        List<Employee> employees = filterEmployee(TestEmployeesLambda.employees, new FilterEmployeeByAge());
        System.out.println(employees);

        List<Employee> employees2 = filterEmployee(TestEmployeesLambda.employees, new FilterEmployeeBySalary());
        System.out.println(employees2);

        /**
         * 优化方式二:匿名内部类
         */
        List<Employee> employees3 = filterEmployee(TestEmployeesLambda.employees, new MyEmployee<Employee>() {
            @Override
            public Boolean test(Employee employee) {
                return employee.getAge() >= 35;
            }
        });
        System.out.println(employees3);

        /**
         * 优化方式三:lambda表达式
         */
        List<Employee> employees4 = filterEmployee(TestEmployeesLambda.employees, employee -> employee.getAge() >= 35);
        System.out.println(employees4);

        /**
         * 优化方式四:stream
         */
        TestEmployeesLambda.employees.stream().filter(employee -> employee.getAge() >= 35).forEach(System.out::println);

        TestEmployeesLambda.employees.stream().map(Employee::getName).forEach(System.out::println);
    }
}
