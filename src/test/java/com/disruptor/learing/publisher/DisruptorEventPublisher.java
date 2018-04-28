package com.disruptor.learing.publisher;

import java.util.concurrent.ThreadFactory;

import com.disruptor.learning.DisruptorEvent;
import com.disruptor.learning.handler.TestHandler;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * Description:disruptor事件发布器
 * All Rights Reserved.
 * @param <T>
 * @version 1.0  2018年4月28日 下午3:24:29  by 代鹏（daipeng.456@gmail.com）创建
 */
public class DisruptorEventPublisher<T> implements EventPublisher<T> {

    private class DisruptorEventHandler implements EventHandler<DisruptorEvent<T>> {

        private TestHandler handler;

        public DisruptorEventHandler(TestHandler handler) {
            this.handler = handler;
        }

        @Override
        public void onEvent(DisruptorEvent<T> event, long sequence, boolean endOfBatch) throws Exception {
            handler.process(event);
        }

    }

    private static final WaitStrategy YIELDING_WAIT = new YieldingWaitStrategy();
    
    private Disruptor<DisruptorEvent<T>> disruptor;

    private DisruptorEventHandler handler;

    private RingBuffer<DisruptorEvent<T>> ringbuffer;

    public DisruptorEventPublisher(int bufferSize, TestHandler handler) {
        this.handler = new DisruptorEventHandler(handler);
        disruptor = new Disruptor<DisruptorEvent<T>>(DisruptorEvent::new, bufferSize, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "test_DisruptorEventPublisher");
            }
        }, ProducerType.SINGLE, YIELDING_WAIT);
    }

    @Override
    public void start() {
        disruptor.handleEventsWith(handler);
        disruptor.start();
        ringbuffer = disruptor.getRingBuffer();
    }

    @Override
    public void publish(T data) throws InterruptedException {
        long seq = ringbuffer.next();
        try {
            DisruptorEvent<T> evt = ringbuffer.get(seq);
            evt.setT(data);
        } finally {
            ringbuffer.publish(seq);
        }
    }

}
