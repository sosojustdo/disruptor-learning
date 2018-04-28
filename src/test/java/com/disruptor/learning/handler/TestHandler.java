package com.disruptor.learning.handler;

import com.disruptor.learning.DisruptorEvent;
import com.disruptor.learning.counter.CounterTracer;

/**
 * Description:事件消费时，计数器计数
 * All Rights Reserved.
 * @version 1.0  2018年4月28日 下午3:40:59  by 代鹏（daipeng.456@gmail.com）创建
 */
public class TestHandler {
  
    private CounterTracer tracer;
    
    public TestHandler(CounterTracer tracer) {
        this.tracer = tracer;
    }
    
    public boolean process(DisruptorEvent<?> event){
        return tracer.count();
    }

}
