package com.zyc.rqueue;

import java.util.Queue;

/**
 * 队列注册
 * @param <T>
 */
public interface ZRQueue<T> extends Queue<T> {

    /**
     * 注册
     * @param queueName
     */
    public void register(String queueName);

    /**
     * 注销
     * @param queueName
     */
    public void unregister(String queueName);

}
