package com.disruptor.learning;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

/**
 * Description:disruptor事件生产者，转换器
 * All Rights Reserved.
 * @param <T>
 * @param <A>
 * @version 1.0  2018年4月16日 下午8:14:37  by 代鹏（daipeng.456@gmail.com）创建
 */
public class DisruptorProducerWithTranslator<T, A extends T>{

    private final RingBuffer<DisruptorEvent<T>> ringBuffer;

    private final EventTranslatorOneArg<DisruptorEvent<T>, A> translator = new EventTranslatorOneArg<DisruptorEvent<T>, A>() {
        @Override
        public void translateTo(DisruptorEvent<T> event, long sequence, A arg0) {
            event.setT(arg0);
        }
    };
    
    public DisruptorProducerWithTranslator(RingBuffer<DisruptorEvent<T>> ringBuffer) { 
        this.ringBuffer = ringBuffer; 
    }
    
    /**
     * Description:投递数据
     * @Version1.0 2018年4月16日 下午8:15:46 by 代鹏（daipeng.456@gmail.com）创建
     * @param a
     */
    public void pubData(A a) {
        ringBuffer.publishEvent(translator, a);
    }

}
