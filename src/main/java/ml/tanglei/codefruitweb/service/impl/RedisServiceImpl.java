package ml.tanglei.codefruitweb.service.impl;

import ml.tanglei.codefruitweb.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    private final static Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean set(String key, Object value) {
        boolean result = false;
        try {
            redisTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key,value);
            result = true;
        } catch (Exception e) {
            logger.error("写入Redis缓存异常，key:" + key,e);
        }
        return result;
    }

    /**
     * 写入缓存设置时间失效时间
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    @Override
    public boolean set(String key, Object value, long expireTime) {
        boolean result = false;
        try {
            redisTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
            ValueOperations<String,Object> operations = redisTemplate.opsForValue();
            operations.set(key,value);
            redisTemplate.expire(key,expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            logger.error("写入Redis缓存异常，key:" + key,e);
        }
        return result;
    }

    /**
     * 批量删除对应key
     * @param keys
     */
    @Override
    public void remove(String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 根据正则表达式批量删除对应key
     * @param pattern
     */
    @Override
    public void removePattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的Value
     * @param key
     */
    @Override
    public void remove(String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否存在对应key
     * @param key
     * @return
     */
    @Override
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存中key对应的值
     * @param key
     * @return
     */
    @Override
    public Object get(String key) {
        Object result = null;
        ValueOperations<String,Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * hash 添加缓存
     * @param key
     * @param hashKey
     * @param value
     */
    @Override
    public void hmSet(String key, Object hashKey, Object value) {
        HashOperations<String,Object,Object> hash = redisTemplate.opsForHash();
        hash.put(key,hashKey,value);
    }

    /**
     * 通过hash获取对应数据
     * @param key
     * @param hashKey
     * @return
     */
    @Override
    public Object hmGet(String key, Object hashKey) {
        HashOperations<String,Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key,hashKey);
    }

    /**
     * 添加List
     * @param key
     * @param value
     */
    @Override
    public void lPush(String key, Object value) {
        ListOperations<String,Object> list = redisTemplate.opsForList();
        list.rightPush(key,value);
    }

    /**
     * 获取List
     * @param key
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<Object> lRange(String key, long start, long end) {
        ListOperations<String,Object> list = redisTemplate.opsForList();
        return list.range(key,start,end);
    }

    /**
     * 添加集合Set
     * @param key
     * @param value
     */
    @Override
    public void add(String key, Object value) {
        SetOperations<String,Object> set = redisTemplate.opsForSet();
        set.add(key,value);
    }

    /**
     * 集合获取Set
     * @param key
     * @return
     */
    @Override
    public Set<Object> setMembers(String key) {
        SetOperations<String,Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 添加有序集合ZSet
     * @param key
     * @param value
     * @param score
     */
    @Override
    public void zAdd(String key, Object value, double score) {
        ZSetOperations<String,Object> zSet = redisTemplate.opsForZSet();
        zSet.add(key,value,score);
    }

    /**
     *获取有序集合ZSet
     * @param key
     * @param score
     * @param score1
     * @return
     */
    @Override
    public Set<Object> rangeByScore(String key, double score, double score1) {
        ZSetOperations<String,Object> zSet = redisTemplate.opsForZSet();
        return zSet.rangeByScore(key,score,score1);
    }
}
