package com.zyc.rqueue;

import cn.hutool.json.JSONUtil;

import java.util.Comparator;

public class RQueuePriorityComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {

        return JSONUtil.toBean(o1, RQueuePriorityInfo.class).getPriority()-JSONUtil.toBean(o2, RQueuePriorityInfo.class).getPriority();
    }
}
