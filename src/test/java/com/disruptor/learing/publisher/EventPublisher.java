package com.disruptor.learing.publisher;

/**
 * Description:定义事件发布
 * All Rights Reserved.
 * @param <T>
 * @version 1.0  2018年4月28日 下午3:21:02  by 代鹏（daipeng.456@gmail.com）创建
 */
public interface EventPublisher<T> {
    
    public void publish(T t) throws InterruptedException;
    
    public void start();
    
}
