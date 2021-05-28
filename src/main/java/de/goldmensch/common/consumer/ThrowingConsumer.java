package de.goldmensch.common.consumer;

//proudly stolen from: https://github.com/eldoriarpg/eldo-util/tree/feature/threading
@FunctionalInterface
public interface ThrowingConsumer<T, Exception extends java.lang.Exception> {

    void accept(T t) throws Exception;
}
