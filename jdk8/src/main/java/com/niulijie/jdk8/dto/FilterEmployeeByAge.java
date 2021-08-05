package com.niulijie.jdk8.dto;

/**
 * @author niuli
 */
public class FilterEmployeeByAge implements MyEmployee<Employee>{

    @Override
    public Boolean test(Employee employee) {
        return employee.getAge() >= 35;
    }
}
