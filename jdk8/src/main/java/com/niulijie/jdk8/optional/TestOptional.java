package com.niulijie.jdk8.optional;

import com.niulijie.jdk8.dto.Customer;

import java.util.Optional;

public class TestOptional {

    public static void main(String[] args) {

        Customer customer = new Customer();
        customer.setAge(13);
        Customer customer1 = Optional.ofNullable(customer).get();
        System.out.println(customer1);
    }
}
