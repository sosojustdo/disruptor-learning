package com.disruptor.learning.counter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Description:简单计数器实现
 * All Rights Reserved.
 * @version 1.0  2018年4月28日 下午3:38:42  by 代鹏（daipeng.456@gmail.com）创建
 */
public class SimpleTracer implements CounterTracer {

    private long startTicks;

    private long endTicks;

    private volatile AtomicLong count = new AtomicLong(0);

    private boolean end = false;

    private volatile AtomicLong expectedCount = new AtomicLong(0);

    private CountDownLatch latch = new CountDownLatch(1);

    public SimpleTracer(AtomicLong expectedCount) {
        this.expectedCount = expectedCount;
    }

    @Override
    public void start() {
        startTicks = System.currentTimeMillis();
        end = false;
    }

    @Override
    public long getMilliTimeSpan() {
        return endTicks - startTicks;
    }

    @Override
    public boolean count() {
        if (end) {
            return end;
        }
        count.incrementAndGet();
        end = count.get() >= expectedCount.get();
        if (end) {
            endTicks = System.currentTimeMillis();
            latch.countDown();
        }
        return end;
    }

    @Override
    public void waitForReached() throws InterruptedException {
        latch.await();
    }

}
