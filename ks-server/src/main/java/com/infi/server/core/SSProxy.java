package com.infi.server.core;

/**
 * @Author: kelvin
 * @Date: 2017-12-26
 * @Company:
 * @Description:
 */
public interface SSProxy<T> {

    void init();

    void send(T t);

    T receive();
}
