package com.zyc.rqueue;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;


/**
 * RQueue统一客户端
 * @param <T>
 */
public class RQueueClient<T>  implements ZRQueue<T> {

    private static String registerKey="rqueue_map";
    private String queueName;
    private RQueueMode rQueueMode;
    private RedissonClient redissonClient;

    private RQueue<T> rQueue;

    private RBlockingDeque<T> blockingDeque;

    public RQueueClient(String queueName, RedissonClient redissonClient,
                        RQueueMode rQueueMode) throws Exception {
        if(redissonClient == null){
            throw new Exception("not connect redisson");
        }
        this.queueName = queueName;
        this.redissonClient = redissonClient;
        this.rQueueMode = rQueueMode;
        buildRQueue(rQueueMode);
    }

    private void buildRQueue(RQueueMode rQueueMode){
        if(rQueueMode == RQueueMode.BLOCKQUEUE){
            rQueue = redissonClient.getBlockingQueue(queueName, StringCodec.INSTANCE);
        }else if(rQueueMode == RQueueMode.DELAYEDQUEUE){
            blockingDeque = redissonClient.getBlockingDeque(queueName, StringCodec.INSTANCE);
            rQueue = (RDelayedQueue<T>)redissonClient.getDelayedQueue(blockingDeque);
        }else if(rQueueMode == RQueueMode.PRIORITYQUEUE){
            RPriorityQueue rPriorityQueue = redissonClient.getPriorityQueue(queueName);
            rPriorityQueue.trySetComparator(new RQueuePriorityComparator());
            rQueue = (RQueue)rPriorityQueue;
        }
    }

    @Override
    public int size() {
        return rQueue.size();
    }

    @Override
    public boolean isEmpty() {
        return rQueue.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return rQueue.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return rQueue.iterator();
    }

    @Override
    public Object[] toArray() {
        return rQueue.toArray(new Object[rQueue.size()]);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return ((RQueue<T>)rQueue).toArray(a);
    }

    @Override
    public boolean add(T t) {
        return rQueue.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return rQueue.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return rQueue.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return rQueue.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return rQueue.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return rQueue.retainAll(c);
    }

    @Override
    public void clear() {
        rQueue.clear();
    }

    @Override
    public boolean equals(Object o) {
        return rQueue.equals(o);
    }

    @Override
    public int hashCode() {
        return rQueue.hashCode();
    }

    @Override
    public boolean offer(T t) {
        return rQueue.offer(t);
    }

    public boolean offer(T t, Long l, TimeUnit timeUnit) {
        if(RQueueMode.DELAYEDQUEUE != rQueueMode){
            return false;
        }
        ((RDelayedQueue<T>)rQueue).offer(t, l, timeUnit);
        return true;
    }

    public boolean offer(Object t, Integer priority) {
        if(RQueueMode.PRIORITYQUEUE != rQueueMode){
            return false;
        }
        RQueuePriorityInfo rQueuePriorityInfo = new RQueuePriorityInfo();
        rQueuePriorityInfo.setT(t);
        rQueuePriorityInfo.setPriority(priority);

        return ((RPriorityQueue<String>)rQueue).offer(JSONUtil.toJsonStr(rQueuePriorityInfo));
    }

    @Override
    public T remove() {
        return ((RQueue<T>)rQueue).remove();
    }

    @Override
    public T poll() {
        if(RQueueMode.PRIORITYQUEUE == rQueueMode){
            T ret =  rQueue.poll();
            if(ret != null){
                RQueuePriorityInfo rQueuePriorityInfo = JSONUtil.toBean(ret.toString(), RQueuePriorityInfo.class);
                return (T)rQueuePriorityInfo.getT();
            }
        }else if(RQueueMode.DELAYEDQUEUE == rQueueMode){
            return blockingDeque.poll();
        }
        return ((RQueue<T>)rQueue).poll();
    }

    @Override
    public T element() {
        if(RQueueMode.PRIORITYQUEUE == rQueueMode){
            RQueuePriorityInfo<T> ret = ((RPriorityQueue<RQueuePriorityInfo>)rQueue).element();
            if(ret != null){
                return ret.getT();
            }
        }else if(RQueueMode.DELAYEDQUEUE == rQueueMode){
            return blockingDeque.element();
        }
        return ((RQueue<T>)rQueue).element();
    }

    @Override
    public T peek() {
        if(RQueueMode.PRIORITYQUEUE == rQueueMode){
            RQueuePriorityInfo<T> ret = ((RPriorityQueue<RQueuePriorityInfo>)rQueue).peek();
            if(ret != null){
                return ret.getT();
            }
        }else if(RQueueMode.DELAYEDQUEUE == rQueueMode){
            return blockingDeque.peek();
        }
        return ((RQueue<T>)rQueue).peek();
    }

    @Override
    public void register(String queueName) {
        //时间
        RQueueInfo rQueueInfo = new RQueueInfo();
        rQueueInfo.setCreate_time(DateUtil.now());
        String value = JSONUtil.toJsonStr(rQueueInfo);
        redissonClient.getMap(registerKey).put(queueName, value);
    }

    @Override
    public void unregister(String queueName) {
        redissonClient.getMap(registerKey).remove(queueName);
    }

    public static class RQueueInfo{
        private String create_time;

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
