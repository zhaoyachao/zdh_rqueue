package com.zyc.rqueue;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于redis的queue管理工具,快速生成RQueueClient
 * 当前queue仅支持redis,需要使用redisson工具
 */
public class RQueueManager {

    private static Map<String, RQueueClient> rQueueClientMap = new ConcurrentHashMap<>();

    private static RedissonClient defaultRedissonClient;

    public static RQueueClient getRQueueClient(String queueName) throws Exception {
        return getRQueueClient(queueName, RQueueMode.BLOCKQUEUE, defaultRedissonClient);
    }

    public static RQueueClient getRQueueClient(String queueName, RedissonClient redissonClient) throws Exception {
        return getRQueueClient(queueName, RQueueMode.BLOCKQUEUE, redissonClient);
    }

    public static RQueueClient getRQueueClient(String queueName, RQueueMode rQueueMode, RedissonClient redissonClient) throws Exception {

        String key = queueName;
        if (rQueueClientMap.containsKey(key)) {
            return rQueueClientMap.get(key);
        }
        //更新client
        RQueueClient rQueueClient = new RQueueClient<String>(queueName, redissonClient, rQueueMode);
        rQueueClientMap.put(key, rQueueClient);
        return rQueueClient;
    }

    public static boolean removeQueue(String name) {
        return removeQueue(name, defaultRedissonClient);
    }

    public static boolean removeQueue(String name, RedissonClient redissonClient) {
        RQueueClient rQueueClient = rQueueClientMap.get(name);
        try {
            rQueueClient.clear();
            rQueueClientMap.remove(name);
        } catch (Exception e) {
            rQueueClientMap.put(name, rQueueClient);
            return false;
        }
        return true;
    }

    /**
     * 快速构建redissonClient
     *
     * @param host
     * @param passwd
     * @return
     */
    public static RedissonClient buildRedissonClient(String host, String passwd) {
        try {
            if(StringUtils.isEmpty(passwd)){
                passwd = null;
            }
            Config config = new Config();
            if (host.contains(",")) {
                config.useClusterServers().addNodeAddress("redis://"+host.split(",")).setPassword(passwd);
            } else {
                config.useSingleServer().setAddress("redis://"+host).setPassword(passwd);
            }
            config.setCodec(StringCodec.INSTANCE);
            RedissonClient redissonClient = Redisson.create(config);
            return redissonClient;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 快速生成默认redissonclient
     * @param host
     * @param passwd
     * @return
     */
    public static RedissonClient buildDefault(String host, String passwd) {
        try {
            synchronized ("default".intern()){
                RedissonClient redissonClient = buildRedissonClient(host, passwd);
                if(defaultRedissonClient == null){
                    defaultRedissonClient = redissonClient;
                }
            }
            return defaultRedissonClient;
        } catch (Exception e) {
            throw e;
        }
    }

    public static RedissonClient reSetDefault(String host, String passwd) {
        try {
            synchronized ("default".intern()){
                RedissonClient redissonClient = buildRedissonClient(host, passwd);
                defaultRedissonClient = redissonClient;
            }
            return defaultRedissonClient;
        } catch (Exception e) {
            throw e;
        }
    }

}
