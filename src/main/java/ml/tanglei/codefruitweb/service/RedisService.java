package ml.tanglei.codefruitweb.service;

import java.util.List;
import java.util.Set;

public interface RedisService {

    boolean set(final String key,Object value);
    boolean set(final String key,Object value,long expireTime);
    void remove(final String... keys);
    void removePattern(final String pattern);
    void remove(final String key);
    boolean exists(final String key);
    Object get(final String key);
    void hmSet(String key,Object hashKey,Object value);
    Object hmGet(String key,Object hashKey);
    void lPush(String key,Object value);
    List<Object> lRange(String key,long start,long end);
    void add(String key, Object value);
    Set<Object> setMembers(String key);
    void zAdd(String key,Object value,double score);
    Set<Object> rangeByScore(String key,double score,double score1);
}
