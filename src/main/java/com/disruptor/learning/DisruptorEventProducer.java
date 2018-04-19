package com.disruptor.learning;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * Description:disruptor 事件发布
 * All Rights Reserved.
 * @version 1.0  2018年4月16日 下午8:25:24  by 代鹏（daipeng.456@gmail.com）创建
 */
public class DisruptorEventProducer<T> {
    
    private T data;//传递的数据
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    /**
     * Description:生产者投递数据
     * @Version1.0 2018年4月17日 下午12:04:40 by 代鹏（daipeng.456@gmail.com）创建
     * @param disruptor
     */
    public void producer(Disruptor<DisruptorEvent<T>> disruptor) {
        try {
            RingBuffer<DisruptorEvent<T>> ringBuffer = disruptor.getRingBuffer();
            DisruptorProducerWithTranslator<T, T> producer = new DisruptorProducerWithTranslator<T, T>(ringBuffer);
            producer.pubData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
