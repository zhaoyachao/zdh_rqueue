package com.zyc.rqueue;

import java.io.Serializable;

public class RQueuePriorityInfo<Object> implements Serializable {
    private Object t;
    private Integer priority;

    public Object getT() {
        return t;
    }

    public void setT(Object t) {
        this.t = t;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
