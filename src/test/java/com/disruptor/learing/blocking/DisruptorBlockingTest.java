package com.disruptor.learing.blocking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.disruptor.learning.DisruptorEvent;
import com.disruptor.learning.DisruptorEventProducer;
import com.disruptor.learning.DisruptorFactory;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorBlockingTest {
    
    @SuppressWarnings("rawtypes")
    private EventHandler[] handlers = {new MyDisruptorEventHandler<List<User>>()};
    private DisruptorEventProducer<List<User>> producer = new DisruptorEventProducer<List<User>>();
    private DisruptorFactory<List<User>> disruptorFactory = new DisruptorFactory<List<User>>();
    private Disruptor<DisruptorEvent<List<User>>> disruptor;
    
    private int bufferSize = 8;
    
    @SuppressWarnings({"unchecked" })
    @Before
    public void init() {
        disruptorFactory.setBufferSize(bufferSize);
        //disruptorFactory.setWaitStrategy(new BusySpinWaitStrategy());
        disruptor = disruptorFactory.getDisruptorInstance(handlers);
    }
    
    @Test
    public void test_publist_Event() throws InterruptedException {
        for (int loop = 1; loop <= 10240; loop++) {
            producer.setData(builderUserList(1024));
            producer.producer(disruptor);
        }
    }
    
    private List<User> builderUserList(int size){
        if(size <= 0) {
            throw new IllegalArgumentException("params size must be more then zero!");
        }
        List<User> users = new ArrayList<User>(size);
        for(int i = 1; i<= size; i++) {
            User u = new User(UUID.randomUUID().toString(), i, UUID.randomUUID().toString());
            users.add(u);
        }
        return users;
    }
    
    class User {
        private String userName;
        private int age;
        private String address;
        
        public User(String userName, int age, String address) {
            super();
            this.userName = userName;
            this.age = age;
            this.address = address;
        }
        public String getUserName() {
            return userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }
        public String getAddress() {
            return address;
        }
        public void setAddress(String address) {
            this.address = address;
        }
    }

}
