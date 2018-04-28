package com.disruptor.learning.performance;

import java.util.UUID;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;

import com.disruptor.learing.publisher.ArrayBlockingQueueEventPublisher;
import com.disruptor.learing.publisher.DisruptorEventPublisher;
import com.disruptor.learning.counter.CounterTracer;
import com.disruptor.learning.counter.SimpleTracer;
import com.disruptor.learning.handler.TestHandler;

/**
 * Description:使用contiperf 压测Disruptor vs ArrayBlockingQueue
 * All Rights Reserved.
 * @version 1.0  2018年4月28日 下午6:02:25  by 代鹏（daipeng.456@gmail.com）创建
 */
public class PerformanceTestWithContiperf {
    
    private static final long loop = 1000l;
    
    @Rule
    public ContiPerfRule contiPerfRule = new ContiPerfRule();
    
    private static CounterTracer tracer = new SimpleTracer(loop);
    private static TestHandler handler = new TestHandler(tracer);
    private static DisruptorEventPublisher<Object> disPublisher;
    private static ArrayBlockingQueueEventPublisher<Object> arrayPublisher;
    
    static {
        System.out.println("init_static_block");
        tracer.start();
        
        disPublisher = new DisruptorEventPublisher<Object>(8192, handler);
        disPublisher.start();
        
        arrayPublisher = new ArrayBlockingQueueEventPublisher<Object>(1024000, handler);
        arrayPublisher.start();
    }
    
    
    
    @Test
    @PerfTest(threads = 1, invocations = 10)
    public void test_contiperf() throws InterruptedException {
        System.out.println("Hello Contiperf!");
    }
    
    @Test
    @PerfTest(threads = 4, invocations = 1000)
    public void contiPerf_Disruptor() throws InterruptedException {
        //EventPublisher<String> publisher = new DisruptorEventPublisher<String>(1024, handler);
        //publisher.start();
        //tracer.start();
                
        disPublisher.publish(UUID.randomUUID().toString());
        tracer.waitForReached();
        System.out.println(tracer.getMilliTimeSpan());
    }
    
    @Test
    @PerfTest(threads = 4, invocations = 1000)
    public void contiPerf_ArrayBlockingQueue() throws InterruptedException {
        //EventPublisher<String> publisher = new ArrayBlockingQueueEventPublisher<String>(1000000, handler);
        //publisher.start();
        //tracer.start();
                
        arrayPublisher.publish(UUID.randomUUID().toString());
        tracer.waitForReached();
        System.out.println(tracer.getMilliTimeSpan());
    }

}
