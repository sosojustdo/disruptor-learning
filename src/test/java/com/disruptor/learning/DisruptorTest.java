package com.disruptor.learning;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * Description:disruptor normal unit case
 * All Rights Reserved.
 * @version 1.0  2018年4月28日 下午2:51:34  by 代鹏（daipeng.456@gmail.com）创建
 */
public class DisruptorTest {
    
    private DisruptorEventProducer<Object> producer;
    private Disruptor<DisruptorEvent<Object>> disruptor;
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Before
    public void init() {
        EventHandler[] handlers = {new MyDisruptorEventHandler1<Set<String>>(), new MyDisruptorEventHandler2<Set<String>>(), new MyDisruptorEventHandler3<Set<String>>()};
        producer = new DisruptorEventProducer<Object>();
        DisruptorFactory<Object> disruptorFactory = new DisruptorFactory<Object>();
        disruptor = disruptorFactory.getDisruptorInstance(handlers);
    }
    
    @Test
    public void test_multi_EventHandler() throws InterruptedException {
        for (long l = 1; l <= 10; l++) {
            Set<String> sets = new HashSet<String>();
            sets.add(UUID.randomUUID().toString());
            producer.setData(sets);
            producer.producer(disruptor);
        }
    }

}
