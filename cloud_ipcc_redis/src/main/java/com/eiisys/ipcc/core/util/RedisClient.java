package com.eiisys.ipcc.core.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

/**
 * Created with IntelliJ IDEA.
 * Created by hujm on 2019/04/15.
 */
@Component
public class RedisClient<V> {

//    private final long DEFAULT_EXPIRE_TIME = 1 * 24 * 60 * 60L;

    public final static long EXPIRE_TIME_ONE_HOUR = 1  * 60 * 60L;

    public final static long EXPIRE_TIME_ONE_MINUTE = 1 * 60L;

    public final static long EXPIRE_TIME_FIVE_MINUTES = 5 * 60L;

    public final static long EXPIRE_TIME_TEN_MINUTES = 10 * 60L;

    public static final long EXPIRE_TIME_HALF_HOUR = 30 * 60L;

    @Resource
    RedisTemplate<String,V> redisTemplate;

    Logger logger = LoggerFactory.getLogger(RedisClient.class);

   
    public void delete(String key){
        
        redisTemplate.delete(key);
    }

    public void delete(Collection<String> keys){
        
        redisTemplate.delete(keys);
    }

    public long incr(String k, Integer l){
        l = l == null ? 0 : l;
        return redisTemplate.opsForValue().increment(k,l);
    }
    
    public long incr(String k,Long l){
        l = l == null ? 0 : l;
        return redisTemplate.opsForValue().increment(k,l);
    }

    public long incr(String k){
        return this.incr(k,1L);
    }

    /**
     * 使用管道形式批量递增数据
     */
    public List<V> incrPipeLine(Map<String, Long> map){
        List<Object> objects = redisTemplate.execute((RedisConnection redisConnection) -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            redisConnection.openPipeline();
            for(String key:map.keySet()){
//                redisConnection.lPush(serializer.serialize(key),serializer.serialize(gson.toJson(map.get(key))));
                redisConnection.incrBy(serializer.serialize(key), map.get(key));
            }
            return redisConnection.closePipeline();
        });
     return (List<V>) objects;
 }
    
    public Long decr(String k,long l){
        return this.incr(k,-l);
    }

    public long decr(String k){
        return this.incr(k, -1L);
    }

    public double incrD(String k,double d){
        return redisTemplate.opsForValue().increment(k,d);
    }

    public double incrD(String k){
        return this.incrD(k, 1D);
    }

    public double decrD(String k,double d){
        return this.incrD(k,-d);
    }

    public double decrD(String k){
        return this.incrD(k,-1D);
    }


    public void set(String k,V v,Long l){
        redisTemplate.opsForValue().set(k,v,l,TimeUnit.SECONDS);
    }

    public void set(String k,V v){
        redisTemplate.opsForValue().set(k,v);
    }

    public void set(String k,V v,Long l,TimeUnit timeUnit){
        redisTemplate.opsForValue().set(k,v,l,timeUnit);
    }

    public V get(String k){
        return redisTemplate.opsForValue().get(k);
    }

    public Boolean expire(String k,int seconds){
        return redisTemplate.expire(k, seconds, TimeUnit.SECONDS);
    }

    public Boolean expire(String k,long seconds){
        return redisTemplate.expire(k, seconds, TimeUnit.SECONDS);
    }
    
    // 列表begin
    public Long lLen(String key) {
        return redisTemplate.opsForList().size(key);
    }
    
    public List<V> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }
    
    public Long lRightPush(String key, V value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }
    
    public Long lRightPush(String key, Collection<V> values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }
    
    public Long lRemove(String key, long count, V value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }
    // 列表end
    
    public List<V> pipeLine(Map<String,V> map){
           List<Object> objects = redisTemplate.execute((RedisConnection redisConnection) -> {
               RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
               Gson gson = new Gson();
               redisConnection.openPipeline();
               for(String key:map.keySet()){
                   redisConnection.lPush(serializer.serialize(key),serializer.serialize(gson.toJson(map.get(key))));
               }
               return redisConnection.closePipeline();
           });
        return (List<V>) objects;
    }


    public boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    public boolean setnx(String key,V v){
        return redisTemplate.opsForValue().setIfAbsent(key,v);
    }
    
    public boolean setnx(String key,V v,Long l){
        boolean isSeted = redisTemplate.opsForValue().setIfAbsent(key, v);
        if (isSeted) redisTemplate.expire(key, l, TimeUnit.SECONDS);
        
        return isSeted;
    }
    
    public Long evalUnlock(String script, List<String> keys, Object...args) {
    	RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
    	return redisTemplate.execute(redisScript, keys, args);
    }
    
    /************************** 集合  **************************/
    /** 集合元素的数量 */
    public Long sCard(String key) {
        return redisTemplate.opsForSet().size(key);
    }
    
    /** 集合添加元素 */
    public Long sAdd(String key, V... values) {
        return redisTemplate.opsForSet().add(key, values);
    }
    
    /** 集合返回所有元素 */
    public Set<V> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }
    
    /** 集合返回部分元素 */
    public Set<V> sRandMember(String key, long count) {
        return redisTemplate.opsForSet().distinctRandomMembers(key, count);
    }
    
    /** 集合移除元素 */
    public Long sRem(String key, V... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }
    
    /************************** 有序集合  **************************/
    /** 有序集合元素的数量 */
    public Long zCard(String key) {
        return redisTemplate.opsForZSet().size(key);
    }
    
    /** 有序集合添加元素 */
    public Boolean zAdd(String key, V value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }
    
    /** 有序集合返回指定区间内的元素 */
    public Set<V> zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }
    
    /** 有序集合移除元素 */
    public Long zRem(String key, V... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }
    
    /** 有序集合移除元素 */
    public Long zRemRangeByScore (String key, double minScore, double maxScore) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, minScore, maxScore);
    }
    
    /** 返回有序集key中成员member的排名 非成员返回null */
    public Long zRank(String key, V value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }
}
