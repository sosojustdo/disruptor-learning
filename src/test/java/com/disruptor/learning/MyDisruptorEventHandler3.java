package com.disruptor.learning;

public class MyDisruptorEventHandler3<T> extends DisruptorEventHandler<T> {

    @Override
    public void onEvent(DisruptorEvent<T> event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("MyDisruptorEventHandler3_onEvent_invoke");
    }
    
}
