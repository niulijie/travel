package com.niulijie.jdk8.dto;

/**
 * @author niuli
 */
public class FilterEmployeeBySalary implements MyEmployee<Employee>{


    @Override
    public Boolean test(Employee employee) {
        return employee.getSalary() >= 5000 ;
    }
}
