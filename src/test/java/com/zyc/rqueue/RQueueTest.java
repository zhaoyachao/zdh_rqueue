package com.zyc.rqueue;

import cn.hutool.core.util.RandomUtil;

public class RQueueTest {

    public static void main(String[] args) throws Exception {
        RQueueManager.buildDefault("127.0.0.1:6379", "");

        RQueueClient rQueueClient = RQueueManager.getRQueueClient("tqueue");
        RQueueClient rQueueClientPriority = RQueueManager.getRQueueClient("tqueue2", RQueueMode.PRIORITYQUEUE);

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){
                    try{
                        Object o = rQueueClientPriority.poll();
                        if(o != null){
                            System.out.println("queue poll: "+o.toString());
                        }
                        Thread.sleep(1000*5);
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
            rQueueClientPriority.offer(random,random);
        }
    }
}
