package com.disruptor.learning;

/**
 * Description:disruptor传递的消息数据
 * All Rights Reserved.
 * @param <T>
 * @version 1.0  2018年4月16日 下午4:01:18  by 代鹏（daipeng.456@gmail.com）创建
 */
public class DisruptorEvent<T> {
    
    private T t;
    
    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

}
