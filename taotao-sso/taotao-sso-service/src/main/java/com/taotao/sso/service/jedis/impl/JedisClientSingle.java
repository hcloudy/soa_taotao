package com.taotao.sso.service.jedis.impl;

import com.taotao.sso.service.jedis.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisClientSingle implements JedisClient{

    @Autowired
    private JedisPool jedisPool;

    @Override
    public String set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.set(key, value);
        jedis.close();
        return result;
    }

    @Override
    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.get(key);
        jedis.close();
        return result;
    }

    @Override
    public Long del(String key) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.del(key);
        return result;
    }

    @Override
    public Long hset(String key, String item, String value) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hset(key, item, value);
        jedis.close();
        return result;
    }

    @Override
    public String hget(String key, String item) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.hget(key,item);
        jedis.close();
        return result;
    }

    @Override
    public Long expire(String item, int second) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.expire(item,second);
        jedis.close();
        return result;
    }

    @Override
    public Long ttl(String item) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.ttl(item);
        jedis.close();
        return result;
    }

    @Override
    public Long hdel(String key, String item) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hdel(key, item);
        jedis.close();
        return result;
    }
}
