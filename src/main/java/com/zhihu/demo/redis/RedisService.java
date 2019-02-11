package com.zhihu.demo.redis;

import com.alibaba.fastjson.JSON;
import com.zhihu.demo.util.ConstantBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 邓超
 * @description Redis服务类
 * @create 2018/9/15
 */
@Service
public class RedisService {

    private JedisPool jedisPool;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ConstantBean constantBean;

    @Autowired
    public void setConstantBean(ConstantBean constantBean) {
        this.constantBean = constantBean;
    }

    @Autowired
    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        /* *
         * 获取单个对象 若未命中则返回null
         * @param [prefix, key, clazz]
         * @return T
         */
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            String str = jedis.get(realKey);
            return stringToBean(str, clazz);
        } finally {
            //释放jedis连接
            returnToPool(jedis);
        }
    }

    public <T> boolean set(KeyPrefix prefix, String key, T value) {
        /* *
         * 设置单个对象 将T类型的 value 序列化,并拼接出正确的key,将(key,value)存入redis
         * @param [prefix, key, value]
         * @return boolean
         */
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            String str = beanToString(value);
            if (str == null || str.length() <= 0) {
                return false;
            }
            int seconds = prefix.expireSeconds();
            if (seconds <= 0) {
                jedis.set(realKey, str);
            } else {
                //给value 加上一个有效期
                String code = jedis.setex(realKey, seconds, str);
                logger.info("setex to redis key {}=> " + code, realKey);
            }
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    public boolean delete(KeyPrefix prefix, String key) {
        /**
         * 删除单个对象
         */
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            jedis.del(realKey);
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    public Long updateExpire(KeyPrefix prefix, String key) {
        /* *
         * 更新过期时间
         * @param [prefix, key]
         * @return boolean
         */
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.expire(realKey, prefix.expireSeconds()); //statusCode
        } finally {
            returnToPool(jedis);
        }
    }

    public boolean exist(KeyPrefix prefix, String key) {
        /* *
         * 判断是否存在
         * @param [prefix, key]
         * @return boolean
         */
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.exists(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    public Long incr(KeyPrefix prefix, String key) {
        /* *
         * 增加值
         * @param [prefix, key]
         * @return java.lang.Long
         */
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.incr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    public Long decr(KeyPrefix prefix, String key) {
        /* *
         * 减少值 如果key不存在 或者值的类型不匹配, 返回-1,(原子操作
         * @param [prefix, key]
         * @return java.lang.Long
         */
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    public <T> boolean rpush(KeyPrefix prefix, String key, T value) {
        /* *
         * 将value存入列表 列表名为(KEY) prefix.getPrefix() + key
         * @param [prefix, key, value]
         * @return boolean
         */
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            String str = beanToString(value);
            if (str == null || str.length() <= 0) {
                return false;
            }
            int seconds = prefix.expireSeconds();
            Long index = jedis.rpush(realKey, str);
            if (index == 1) {//列表刚创建
                if (seconds > 0) {
                    Long code = jedis.expire(realKey, seconds);
                    logger.info("expire to redis key {} => " + code, realKey);
                }
            }
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    public <T> List<T> getList(KeyPrefix prefix, String key, Class<T> clazz) {
        /* *
         * 获取一个列表 若未命中则返回null
         * @param [prefix, key, clazz]
         * @return T
         */
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            List<String> list = jedis.lrange(realKey, 0, -1);
            List<T> arrayList = new ArrayList<>();
            for (String aList : list) {
                arrayList.add(stringToBean(aList, clazz));
            }
            return arrayList;
        } finally {
            //释放jedis连接
            returnToPool(jedis);
        }
    }

    public <T> boolean addHistory(int id1, int id2, T value) {
        /* *
         * 存储聊天记录 上限100条 超过自动删除
         * id小的在前面
         * @param [prefix, key, value]
         * @return boolean
         */
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = id1 < id2 ? "" + id1 + "-" + id2 : "" + id2 + "-" + id1;//小的在前面
            String str = beanToString(value);
            if (str == null || str.length() <= 0) {
                return false;
            }
            Long index = jedis.lpush(realKey, str);
            if (index > constantBean.getMaxhistory()) {//列表刚创建
                jedis.ltrim(realKey, 0, constantBean.getMaxhistory());
            }
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    public <T> List<T> getHistory(int id1, int id2, int offset, Class<T> clazz) {
        /* *
         * 一次刷20条历史记录 只能在特定的两个人之间
         * @param [prefix, key, clazz]
         * @return T
         */
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = id1 < id2 ? "" + id1 + "-" + id2 : "" + id2 + "-" + id1;//小的在前面
            List<String> list = jedis.lrange(realKey, offset * 20, offset * 20 + 20); //当offset超过5的时候 返回结果永远为null
            List<T> arrayList = new ArrayList<>();
            for (String aList : list) {
                arrayList.add(stringToBean(aList, clazz));
            }
            return arrayList;
        } finally {
            //释放jedis连接
            returnToPool(jedis);
        }
    }


     <T> String beanToString(T value) {
        //序列化
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else {
            //对于其他类型 当作bean处理
            /* * 5
             * JavaBean是特殊的Java类
             * 1.提供一个默认的无参构造函数。
             * 2.需要被序列化并且实现了Serializable接口。
             * 3.可能有一系列可读写属性。
             * 4.可能有一系列的"getter"或"setter"方法。
             */
            return JSON.toJSONString(value);
        }
    }

    @SuppressWarnings("unchecked")
     <T> T stringToBean(String str, Class<T> clazz) {
        //反序列化
        if (str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

     void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }


}
