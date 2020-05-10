package com.github.dragonhht.redis.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redis的分布式锁.
 *
 * @author: huang
 * @Date: 2019-6-23
 */
public class RedisLock {

    private static JedisPool jedisPool;

    public static JedisPool getJedisPool() {
        if (jedisPool != null) {
            return jedisPool;
        }
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(10);
        config.setMaxIdle(5);
        jedisPool = new JedisPool(config, "my.dragon.com", 6379);
        return jedisPool;
    }

    public static Jedis getJedis() {
        return getJedisPool().getResource();
    }

    /**
     * 获取锁
     * @return
     */
    public String getLock(String key, int timeout) {
        try {
            Jedis jedis = getJedis();
            String value = UUID.randomUUID().toString();
            long end = System.currentTimeMillis() + timeout;
            // 使用NX设置键值
            while (System.currentTimeMillis() < end) {
                if (jedis.setnx(key, value) == 1) {
                    return value;
                }
                TimeUnit.MINUTES.sleep(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 释放锁
     * @return
     */
    public boolean releaseLock(String key, String value) {
        try {
            Jedis jedis = getJedis();
            while (true) {
                jedis.watch(key);
                if (value.equals(jedis.get(key))) {
                    // 获取事务
                    Transaction transaction = jedis.multi();
                    transaction.del(key);
                    List<Object> list = transaction.exec();
                    if (list == null) {
                        continue;
                    }
                    return true;
                }
                jedis.unwatch();
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        RedisLock lock = new RedisLock();
        String key = "spring:redis:lock";
        String lockId = lock.getLock(key, 3000);
        System.out.println(Thread.currentThread().getName() + " : 获取锁");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lockId != null) {
                lock.releaseLock(key, lockId);
                System.out.println(Thread.currentThread().getName() + " : 释放锁");
            }
        }
    }

}
