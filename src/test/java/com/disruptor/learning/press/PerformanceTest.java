package com.disruptor.learning.press;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import com.disruptor.learing.publisher.ArrayBlockingQueueEventPublisher;
import com.disruptor.learing.publisher.DisruptorEventPublisher;
import com.disruptor.learing.publisher.EventPublisher;
import com.disruptor.learning.counter.CounterTracer;
import com.disruptor.learning.counter.SimpleTracer;
import com.disruptor.learning.handler.TestHandler;

/**
 * Description:性能测试用例
 * All Rights Reserved.
 * @version 1.0  2018年4月28日 下午4:45:40  by 代鹏（daipeng.456@gmail.com）创建
 */
public class PerformanceTest {
    
    private static final long loop = 1000000l;
    private CountDownLatch latch = new CountDownLatch(1);
    
    @Test
    public void test_Disruptor() throws InterruptedException {
        CounterTracer tracer = new SimpleTracer(loop);
        TestHandler handler = new TestHandler(tracer);
                
        EventPublisher<Integer> publisher = new DisruptorEventPublisher<Integer>(1024, handler);
        publisher.start();
        tracer.start();
                
        for (int i = 0; i < loop; i++) {
            publisher.publish(i);
        }
        tracer.waitForReached();
        System.out.println(tracer.getMilliTimeSpan());
        latch.await();
    }
    
    @Test
    public void test_ArrayBlockingQueue() throws InterruptedException {
        CounterTracer tracer = new SimpleTracer(loop);
        TestHandler handler = new TestHandler(tracer);
                
        EventPublisher<Integer> publisher = new ArrayBlockingQueueEventPublisher<Integer>(1000000, handler);
        publisher.start();
        tracer.start();
                
        for (int i = 0; i < loop; i++) {
            publisher.publish(i);
        }
        tracer.waitForReached();
        System.out.println(tracer.getMilliTimeSpan());
        latch.await();
    }

}
