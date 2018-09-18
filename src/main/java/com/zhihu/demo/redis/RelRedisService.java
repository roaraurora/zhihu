package com.zhihu.demo.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RelRedisService {
    private JedisPool jedisPool;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private RedisService redisService;

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Autowired
    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public boolean setBit(KeyPrefix prefix, String key, long id, boolean value) {
        /* *
         * bitmap 将第id位的bit取反
         * @param [prefix, key, value]
         * @return boolean
         */
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            logger.info("set redis bitmap ID => "+id);
            jedis.setbit(realKey, id, value);
            return true;
        } finally {
            redisService.returnToPool(jedis);
        }
    }

    public boolean getBit(KeyPrefix prefix, String key, long id) {
        /* *
         * 获取bitmap中第id位的对象
         * @param [prefix, key]
         * @return T
         */
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.getbit(realKey, id);
        } finally {
            //释放jedis连接
            redisService.returnToPool(jedis);
        }
    }

    public Long getCount(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.bitcount(realKey);
        } finally {
            //释放jedis连接
            redisService.returnToPool(jedis);
        }
    }
}
