package com.disruptor.learning;

public class MyDisruptorEventHandler2<T> extends DisruptorEventHandler<T> {

    @Override
    public void onEvent(DisruptorEvent<T> event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("MyDisruptorEventHandler2_onEvent_invoke");
    }
    
}
