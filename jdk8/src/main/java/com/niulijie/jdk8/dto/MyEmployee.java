package com.niulijie.jdk8.dto;

/**
 * @author niuli
 */
@FunctionalInterface
public interface MyEmployee<T> {

    Boolean test(T t);
}
