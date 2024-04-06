package com.zyc.rqueue;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RQueueTest2 {

    public static void main(String[] args) throws Exception {
        RQueueManager.buildDefault("127.0.0.1:6379", "");

        RQueueClient rQueueClientDelayed = RQueueManager.getRQueueClient("tqueue3", RQueueMode.DELAYEDQUEUE);

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){
                    try{
                        Object o = rQueueClientDelayed.poll();
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
            Thread.sleep(1000*5);
            //rQueueClient.add(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            int random = RandomUtil.getRandom().nextInt();
            System.out.println(random);
            rQueueClientDelayed.offer("123_"+DateUtil.format(DateUtil.offsetSecond(new Date(), 30), "yyyyMMddHHmmss"), 30L, TimeUnit.SECONDS);
        }
    }
}
