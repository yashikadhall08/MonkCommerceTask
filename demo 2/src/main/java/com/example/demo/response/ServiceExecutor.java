package com.example.demo.response;

@FunctionalInterface
public interface ServiceExecutor<T> {
    T execute();
}
