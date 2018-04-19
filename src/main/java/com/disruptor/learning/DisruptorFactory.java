package com.disruptor.learning;

import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * Description:获取Disruptor实例工厂类
 * All Rights Reserved.
 * @version 1.0  2018年4月17日 下午2:57:49  by 代鹏（daipeng.456@gmail.com）创建
 */
public class DisruptorFactory<T> {

    private int bufferSize = 2048;// bufferSize大小,默认值:2048

    private String threadName = "disruptor-self-thread";// 线程名称

    private ProducerType producerType = ProducerType.SINGLE;// 事件发布类型:SINGLE(单个)；MULTI(多个)

    private WaitStrategy waitStrategy = new BlockingWaitStrategy();

    private Disruptor<DisruptorEvent<T>> disruptor;
        
    public DisruptorFactory() {
        super();
    }

    public DisruptorFactory(int bufferSize, String threadName) {
        super();
        this.bufferSize = bufferSize;
        this.threadName = threadName;
    }
    
    public DisruptorFactory(int bufferSize, String threadName, ProducerType producerType, WaitStrategy waitStrategy) {
        super();
        this.bufferSize = bufferSize;
        this.threadName = threadName;
        this.producerType = producerType;
        this.waitStrategy = waitStrategy;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public ProducerType getProducerType() {
        return producerType;
    }

    public void setProducerType(ProducerType producerType) {
        this.producerType = producerType;
    }

    public WaitStrategy getWaitStrategy() {
        return waitStrategy;
    }

    public void setWaitStrategy(WaitStrategy waitStrategy) {
        this.waitStrategy = waitStrategy;
    }

    /**
     * Description:获取Disruptor实例
     * @Version1.0 2018年4月17日 下午5:07:49 by 代鹏（daipeng.456@gmail.com）创建
     * @return
     */
    public Disruptor<DisruptorEvent<T>> getDisruptorInstance(EventHandler<? super DisruptorEvent<T>> handler) {
        if (null == disruptor) {
            disruptor = new Disruptor<DisruptorEvent<T>>(DisruptorEvent::new, bufferSize, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, threadName);
                }
            }, producerType, waitStrategy);
            disruptor.handleEventsWith(handler);
            disruptor.start();
        }
        return disruptor;
    }

}
