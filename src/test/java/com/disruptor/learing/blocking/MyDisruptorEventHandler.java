package com.disruptor.learing.blocking;

import com.disruptor.learning.DisruptorEvent;
import com.disruptor.learning.DisruptorEventHandler;

public class MyDisruptorEventHandler<T> extends DisruptorEventHandler<T> {

    @SuppressWarnings("static-access")
    @Override
    public void onEvent(DisruptorEvent<T> event, long sequence, boolean endOfBatch) throws Exception {
        //always block forever
        Thread.currentThread().sleep(Integer.MAX_VALUE);
        //System.out.println("MyDisruptorEventHandler_onEvent_invoke");
    }
    
}
