package com.zhihu.demo.redis;

import com.zhihu.demo.vo.NeterVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 邓超
 * @description Redis关系服务类
 * @create 2018/9/15
 */
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
            logger.info("set redis bitmap ID => " + id);
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

    public Long getCount(KeyPrefix prefix, String key) {
        /*
           获取bitmap中为1的位数
         */
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

    /**
     * zscore key value 判断是否存在 nil | score
     * zadd key score value 添加元素 O(M*log(N)) N:length M: number
     * zrem key value 删除元素 O(M*log(N)) N:length M: number
     * zcard key 总元素 O(1)
     * zrange key start start+offset 区间取元素 按score从小到大排序 O(log(N)+M) N:length M: number
     */
    public <T> boolean zadd(KeyPrefix prefix, String key, T value) {
        /* *
         * 以unix时间戳为score存入zset
         * @param [prefix, key, value]
         * @return boolean
         */
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            String str = redisService.beanToString(value);
            if (str == null || str.length() <= 0) {
                return false;
            }
            long timemillis = System.currentTimeMillis();
            jedis.zadd(realKey, timemillis, str);
            return true;
        } finally {
            redisService.returnToPool(jedis);
        }
    }

    public <T> Double zscore(KeyPrefix prefix, String key, T value) {
        /* *
         * 获取单个对象的score 若未命中则返回nil
         * @param [prefix, key, clazz]
         * @return T
         */
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            String str = redisService.beanToString(value);
            return jedis.zscore(realKey, str);
        } finally {
            //释放jedis连接
            redisService.returnToPool(jedis);
        }
    }

    public <T> boolean zrem(KeyPrefix prefix, String key, T value) {
        /*
         * 删除单个对象
         */
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            String str = redisService.beanToString(value);
            jedis.zrem(realKey, str);
            return true;
        } finally {
            redisService.returnToPool(jedis);
        }
    }

    public Long zcard(KeyPrefix prefix, String key) {
        /*
           获取zset的元素的个数
         */
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.zcard(realKey);
        } finally {
            //释放jedis连接
            redisService.returnToPool(jedis);
        }
    }

    public <T> Set<T> zrange(KeyPrefix prefix, String key, long page, long offset, Class<T> clazz) {
        /* *
         * 一次刷20个关注对象 按关注时间排序
         * @param [prefix, key, clazz]
         * @return T
         */
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            Set<String> set = jedis.zrange(realKey, page * offset, page * offset + offset);
            Set<T> hashSet = new HashSet<>();
            for (String string : set) {
                hashSet.add(redisService.stringToBean(string, clazz));
            }
            return hashSet;
        } finally {
            //释放jedis连接
            redisService.returnToPool(jedis);
        }
    }

    public <T> boolean zremNet(String followerId, String fansId, T follower, T fans) {
        /* *
         * 同时为关注者取消关注对象 为被关注者取消粉丝对象 并使用事物
         * @param [prefix, key, value]
         * @return boolean
         */
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Transaction t = jedis.multi(); //开启redi事物
            String fansKey = UserKey.followKey.getPrefix() + fansId;
            String strFollower = redisService.beanToString(follower);
            if (strFollower == null || strFollower.length() <= 0) {
                return false;
            }
            t.zrem(fansKey, strFollower); //把目标用户加入到user的关注列表中
            String followKy = UserKey.fansKey.getPrefix() + followerId;
            String strFans = redisService.beanToString(fans);
            if (strFans == null || strFans.length() <= 0) {
                return false;
            }
            t.zrem(followKy, strFans);//把user加入到目标用户的粉丝列表中
            t.exec();
            return true;
        } finally {
            redisService.returnToPool(jedis);
        }
    }

    public <T> boolean zaddNet(String followerId, String fansId, T follower, T fans) {
        /* *
         * 同时为关注者添加关注对象 为被关注者添加粉丝对象 并使用事物
         * @param [prefix, key, value]
         * @return boolean
         */
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Transaction t = jedis.multi(); //开启redi事物
            String fansKey = UserKey.followKey.getPrefix() + fansId;
            String strFollower = redisService.beanToString(follower);
            if (strFollower == null || strFollower.length() <= 0) {
                return false;
            }
            long timemillis = System.currentTimeMillis();
            t.zadd(fansKey, timemillis, strFollower); //把目标用户加入到user的关注列表中
            String followKy = UserKey.fansKey.getPrefix() + followerId;
            String strFans = redisService.beanToString(fans);
            if (strFans == null || strFans.length() <= 0) {
                return false;
            }
            t.zadd(followKy, timemillis, strFans);//把user加入到目标用户的粉丝列表中
            t.exec();
            return true;
        } finally {
            redisService.returnToPool(jedis);
        }
    }
}
