package com.disruptor.learning.performance;

import java.util.ArrayList;
import java.util.List;
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
    
    private static final long loop = 1000l*1000l;
    private static final int nThreads = 1;
    
    private ExecutorService executor = Executors.newFixedThreadPool(nThreads);
    
    private CountDownLatch latch = new CountDownLatch(1);
    
    private CounterTracer tracer = new SimpleTracer(new AtomicLong(loop));
    private TestHandler handler = new TestHandler(tracer);
    private DisruptorEventPublisher<Object> disPublisher;
    private ArrayBlockingQueueEventPublisher<Object> arrayPublisher;
    
    //buffer size must be a power of 2 
    private int disruptorBufferSize = 1048576;
    private int blockingQueueCapacity = 1048576;
    
    @Before
    public void init() {
        tracer.start();
        
        disPublisher = new DisruptorEventPublisher<Object>(disruptorBufferSize, handler);
        disPublisher.start();
        
        arrayPublisher = new ArrayBlockingQueueEventPublisher<Object>(blockingQueueCapacity, handler);
        arrayPublisher.start();
    }
    
    @Test
    public void test_Disruptor_multiple_thread() throws InterruptedException, ExecutionException {
        List<DisruptorTask> tasks = new ArrayList<DisruptorTask>();
        for(int i=1; i<=1000; i++) {
            DisruptorTask task = new DisruptorTask(tracer, disPublisher, loop);
            tasks.add(task);
        }
        long start = System.currentTimeMillis();
        executor.invokeAll(tasks);
        System.out.println((System.currentTimeMillis() - start) + "ms");
        latch.await();
    }
    
    @Test
    public void test_ArrayBlockingQueue_multiple_thread() throws InterruptedException, ExecutionException {
        List<ArrayBlockingQueueTask> tasks = new ArrayList<ArrayBlockingQueueTask>();
        for(int i=1; i<=1000; i++) {
            ArrayBlockingQueueTask task = new ArrayBlockingQueueTask(tracer, arrayPublisher, loop);
            tasks.add(task);
        }
        long start = System.currentTimeMillis();
        executor.invokeAll(tasks);
        System.out.println((System.currentTimeMillis() - start) + "ms");
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
