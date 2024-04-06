package com.zyc.rqueue;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RQueueTest3 {

    public static void main(String[] args) throws Exception {
        RedissonClient redissonClient = RQueueManager.buildDefault("127.0.0.1:6379", "");

        RBlockingDeque rQueue = redissonClient.getBlockingDeque("tqueue5", StringCodec.INSTANCE);
        RDelayedQueue rQueueClientDelayed = redissonClient.getDelayedQueue(rQueue);
        RBlockingDeque rQueue2 = redissonClient.getBlockingDeque("tqueue5", StringCodec.INSTANCE);
        RDelayedQueue rQueueClientDelayed2 = redissonClient.getDelayedQueue(rQueue2);
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){
                    try{
                        Object o = rQueue2.poll();
                        if(o != null){
                            System.out.println("queue poll: "+DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss")+": "+o.toString());
                        }
                        Thread.sleep(0);
                    }catch (Exception e){

                    }
                }

            }
        }).start();

        while (true){
            Thread.sleep(1000*2);
            //rQueueClient.add(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            int random = RandomUtil.getRandom().nextInt();
            System.out.println(random);
            rQueueClientDelayed.offer("123_"+DateUtil.format(DateUtil.offsetSecond(new Date(), 30), "yyyyMMddHHmmss"), 30L, TimeUnit.SECONDS);
        }
    }
}
