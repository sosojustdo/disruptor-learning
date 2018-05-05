package com.disruptor.learning.performance;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;

import com.disruptor.learing.publisher.ArrayBlockingQueueEventPublisher;
import com.disruptor.learing.publisher.DisruptorEventPublisher;
import com.disruptor.learning.counter.CounterTracer;
import com.disruptor.learning.counter.SimpleTracer;
import com.disruptor.learning.handler.TestHandler;

public class PerformaceTestConcurrent {
    
    private static final long loop = 10000000l;
    private static final int nThreads = 1; 
    
    private ExecutorService executor = Executors.newFixedThreadPool(nThreads);
    
    private CountDownLatch latch = new CountDownLatch(1);
    
    private CounterTracer tracer = new SimpleTracer(new AtomicLong(loop));
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
    public void test_Disruptor_multiple_thread() throws InterruptedException, ExecutionException {
        DisruptorTask task = new DisruptorTask(tracer, disPublisher, loop);
        Future<Long> result = executor.submit(task);
        System.out.println(result.get() + "ms");
        latch.await();
    }
    
    @Test
    public void test_ArrayBlockingQueue_multiple_thread() throws InterruptedException, ExecutionException {
        ArrayBlockingQueueTask task = new ArrayBlockingQueueTask(tracer, arrayPublisher, loop);
        Future<Long> result = executor.submit(task);
        System.out.println(result.get() + "ms");
        latch.await();
    }
    
    class DisruptorTask implements Callable<Long>{
        private CounterTracer tracer;
        private DisruptorEventPublisher<Object> disPublisher;
        private long loop;

        public DisruptorTask(CounterTracer tracer, DisruptorEventPublisher<Object> disPublisher, long loop) {
            super();
            this.tracer = tracer;
            this.disPublisher = disPublisher;
            this.loop = loop;
        }
        
        @Override
        public Long call() throws Exception {
            for (int i = 0; i < loop; i++) {
                disPublisher.publish(i);
            }
            tracer.waitForReached();
            return tracer.getMilliTimeSpan();
        }
        
    }
    
    class ArrayBlockingQueueTask implements Callable<Long>{
        private CounterTracer tracer;
        private ArrayBlockingQueueEventPublisher<Object> arrayPublisher;
        private long loop;
        
        public ArrayBlockingQueueTask(CounterTracer tracer, ArrayBlockingQueueEventPublisher<Object> arrayPublisher, long loop) {
            super();
            this.tracer = tracer;
            this.arrayPublisher = arrayPublisher;
            this.loop = loop;
        }
        
        @Override
        public Long call() throws Exception {
            for (int i = 0; i < loop; i++) {
                arrayPublisher.publish(i);
            }
            tracer.waitForReached();
            return tracer.getMilliTimeSpan();
        }
        
    }
    
    
    

}
