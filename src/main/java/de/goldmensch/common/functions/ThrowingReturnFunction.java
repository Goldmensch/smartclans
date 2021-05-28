package de.goldmensch.common.functions;

//proudly stolen from: https://github.com/eldoriarpg/eldo-util/tree/feature/threading
public interface ThrowingReturnFunction<R, T, E extends Exception> {
    R apply(T t) throws E;
}
