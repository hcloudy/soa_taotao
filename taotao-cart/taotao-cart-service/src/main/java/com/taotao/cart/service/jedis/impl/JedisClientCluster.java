package com.taotao.cart.service.jedis.impl;

import com.taotao.cart.service.jedis.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

import java.util.Map;

public class JedisClientCluster implements JedisClient{

    @Autowired
    private JedisCluster jedisCluster;

    @Override
    public String set(String key, String value) {
        String result = jedisCluster.set(key, value);
        return result;
    }

    @Override
    public String get(String key) {
        return jedisCluster.get(key);
    }

    @Override
    public Long del(String key) {
        return jedisCluster.del(key);
    }

    @Override
    public Long hset(String key, String item, String value) {
        return jedisCluster.hset(key, item, value);
    }

    @Override
    public String hget(String key, String item) {
        return jedisCluster.hget(key,item);
    }

    @Override
    public Long expire(String item, int second) {
        return jedisCluster.expire(item,second);
    }

    @Override
    public Long ttl(String item) {
        return jedisCluster.ttl(item);
    }

    @Override
    public Long hdel(String key, String item) {
        return jedisCluster.hdel(key,item);
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        Map<String, String> map = jedisCluster.hgetAll(key);
        return map;
    }
}
