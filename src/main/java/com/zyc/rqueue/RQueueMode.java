package com.zyc.rqueue;

public enum RQueueMode {
    BLOCKQUEUE("block","阻塞无界队列"),DELAYEDQUEUE("delayed","延迟队列"),PRIORITYQUEUE("priority","优先级队列");

    private String code;
    private String desc;
    private RQueueMode(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String code(){
        return this.code;
    }

    public String desc(){
        return this.desc;
    }
}
