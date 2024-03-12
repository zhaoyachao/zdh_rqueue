package com.zyc.rqueue;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

public class RQueueTest {

    public static void main(String[] args) throws Exception {
        RQueueManager.buildDefault("127.0.0.1:6379", "zyczzu");

        RQueueClient rQueueClient = RQueueManager.getRQueueClient("tqueue");

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){
                    try{
                        Object o = rQueueClient.poll();
                        if(o != null){
                            System.out.println("queue poll: "+o.toString());
                        }
                        Thread.sleep(0);
                    }catch (Exception e){

                    }
                }

            }
        }).start();

        while (true){
            Thread.sleep(1000*5);
            rQueueClient.add(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        }

    }
}
