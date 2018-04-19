package com.disruptor.learning;

import com.lmax.disruptor.EventHandler;

/**
 * Description:Disruptor 消息消费处理器，各个子业务基础该类重写onEvent方法
 * All Rights Reserved.
 * @param <T>
 * @version 1.0  2018年4月16日 下午8:12:27  by 代鹏（daipeng.456@gmail.com）创建
 */
public class DisruptorEventHandler<T> implements EventHandler<DisruptorEvent<T>> {
    
    @Override
    public void onEvent(DisruptorEvent<T> event, long sequence, boolean endOfBatch) throws Exception {
        // TODO Auto-generated method stub
    }
    
}
