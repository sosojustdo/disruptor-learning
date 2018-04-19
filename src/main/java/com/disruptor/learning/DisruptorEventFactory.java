package com.disruptor.learning;

import com.lmax.disruptor.EventFactory;

/**
 * Description:传递消息数据实体的工厂类
 * All Rights Reserved.
 * @version 1.0  2018年4月16日 下午4:20:46  by 代鹏（daipeng.456@gmail.com）创建
 * @param <T>
 */
public class DisruptorEventFactory<T> implements EventFactory<DisruptorEvent<T>> {

    @Override
    public DisruptorEvent<T> newInstance() {
        return new DisruptorEvent<T>();
    }

}
