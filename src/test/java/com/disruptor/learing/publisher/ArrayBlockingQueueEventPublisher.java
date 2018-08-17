package com.disruptor.learing.publisher;

import java.util.concurrent.ArrayBlockingQueue;

import com.disruptor.learning.DisruptorEvent;
import com.disruptor.learning.handler.TestHandler;

/**
 * Description:ArrayBlockingQueue事件发布器
 * All Rights Reserved.
 * @param <T>
 * @version 1.0  2018年4月28日 下午3:25:35  by 代鹏（daipeng.456@gmail.com）创建
 */
public class ArrayBlockingQueueEventPublisher<T> implements EventPublisher<T> {

    private ArrayBlockingQueue<DisruptorEvent<T>> queue;

    private TestHandler handler;
    
    public ArrayBlockingQueueEventPublisher(int maxEventSize, TestHandler handler) {
        this.queue = new ArrayBlockingQueue<DisruptorEvent<T>>(maxEventSize);
        this.handler = handler;
    }

    @Override
    public void start() {
        //System.out.println("ArrayBlockingQueue");
        Thread thrd = new Thread(new Runnable() {
            @Override
            public void run() {
                handle();
            }
        });
        thrd.start();
    }

    private void handle() {
        try {
            DisruptorEvent<T> evt;
            while (true) {
                evt = queue.take();
                if (evt != null && handler.process(evt)) {
                    // 完成后自动结束处理线程;
                    System.out.println("ArrayBlockingQueue task finished...");
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void publish(T t) throws InterruptedException {
        DisruptorEvent<T> evt = new DisruptorEvent<T>();
        evt.setT(t);
        queue.put(evt);
    }

}
