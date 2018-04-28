package com.disruptor.learning.performance;

import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;

import com.disruptor.learing.publisher.ArrayBlockingQueueEventPublisher;
import com.disruptor.learing.publisher.DisruptorEventPublisher;
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
    
    private CounterTracer tracer = new SimpleTracer(loop);
    private TestHandler handler = new TestHandler(tracer);
    private DisruptorEventPublisher<Object> disPublisher;
    private ArrayBlockingQueueEventPublisher<Object> arrayPublisher;
    
    @Before
    public void init() {
        tracer.start();
        
        disPublisher = new DisruptorEventPublisher<Object>(8192, handler);
        disPublisher.start();
        
        arrayPublisher = new ArrayBlockingQueueEventPublisher<Object>(1024000, handler);
        arrayPublisher.start();
    }
    
    @Test
    public void test_Disruptor() throws InterruptedException {
        for (int i = 0; i < loop; i++) {
            disPublisher.publish(i);
        }
        tracer.waitForReached();
        System.out.println(tracer.getMilliTimeSpan());
        latch.await();
    }
    
    @Test
    public void test_ArrayBlockingQueue() throws InterruptedException {
        for (int i = 0; i < loop; i++) {
            arrayPublisher.publish(i);
        }
        tracer.waitForReached();
        System.out.println(tracer.getMilliTimeSpan());
        latch.await();
    }
    
    

}
