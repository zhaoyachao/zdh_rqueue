package com.zyc.rqueue;


public class RQueueTest4 {

    public static void main(String[] args) throws Exception {
        RQueueManager.buildDefault("127.0.0.1:6379", "");

        RQueueClient rQueueClientPriority = RQueueManager.getRQueueClient("tqueue6", RQueueMode.PRIORITYQUEUE);


        rQueueClientPriority.offer("m1",1);

        System.out.println(rQueueClientPriority.contains("m1", 1));
        System.out.println(rQueueClientPriority.remove("m1",1));
        System.out.println(rQueueClientPriority.contains("m1", 1));

        System.out.println(rQueueClientPriority.poll());
    }
}
