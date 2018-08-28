package com.disruptor.learning.falsesharing;

/**
 * Description:false sharing test
 * All Rights Reserved.
 * @version 1.0  2018年8月17日 上午11:11:24  by 代鹏（daipeng.456@gmail.com）创建
 */
public class FalseSharingTest implements Runnable{
    
    public final static int threadNum = 8;
    
    public final static long loops = 1000l*1000l*1000l;

    private final int index;
    
    public FalseSharingTest(final int index) {
        this.index = index;
    }
    
    private static VolatileLong[] longs0 = new VolatileLong[threadNum];
    static {
        for (int i = 0; i < longs0.length; i++) {
            longs0[i] = new VolatileLong();
        }
    }
    
    private static VolatilePaddingLong[] longs1 = new VolatilePaddingLong[threadNum];
    static {
        for (int i = 0; i < longs1.length; i++) {
            longs1[i] = new VolatilePaddingLong();
        }
    }
    
    private static VolatileAnnotationContendedLong[] longs2 = new VolatileAnnotationContendedLong[threadNum];
    static {
        for (int i = 0; i < longs2.length; i++) {
            longs2[i] = new VolatileAnnotationContendedLong();
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        long start = System.nanoTime();
        runTest();
        System.out.println("duration = " + (System.nanoTime() - start));
    }
    
    private static void runTest() throws InterruptedException {
        Thread[] threads = new Thread[threadNum];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharingTest(i));
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }
    }
    
    @Override
    public void run() {
        long i = loops + 1;
        while (0 != --i) {
            //longs0[index].value = i;
            //longs1[index].value = i;
            longs2[index].value = i;
        }
    }
    
    public final static class VolatileLong {
        public volatile long value = 0L;
    }
    
    public final static class VolatilePaddingLong {
        public volatile long value = 0;
        private long p1, p2, p3, p4, p5, p6;
    }
    
    //-XX:-RestrictContended
    @SuppressWarnings("restriction")
    @sun.misc.Contended
    public final static class VolatileAnnotationContendedLong {
        public volatile long value = 0;
    }

}
