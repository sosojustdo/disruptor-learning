package com.disruptor.learning.counter;

/**
 * Description:计数器接口定义
 * All Rights Reserved.
 * @version 1.0  2018年4月28日 下午3:38:22  by 代鹏（daipeng.456@gmail.com）创建
 */
public interface CounterTracer {
    
    public void start();
    
    public long getMilliTimeSpan();
    
    public boolean count();
    
    public void waitForReached()throws InterruptedException;

}
