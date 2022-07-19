package com.mahama.common.utils;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisHelp {
    public interface RedisTask<R> {
        R execute();
    }

    public interface RedisVoidTask {
        void execute();
    }

    private final RedisTemplate<String, Object> redisTemplate;
    private final String keyPrefix;
    private boolean disabled;

    private static final String idKey = "id_";

    public static String getIdKey() {
        return idKey;
    }

    public RedisHelp(RedisTemplate<String, Object> redisTemplate) {
        this(redisTemplate, "");
    }

    public RedisHelp(RedisTemplate<String, Object> redisTemplate, String keyPrefix) {
        this.redisTemplate = redisTemplate;
        this.keyPrefix = keyPrefix;
    }

    public RedisHelp disabled(boolean _disabled) {
        this.disabled = _disabled;
        return this;
    }

    public <R> R query(RedisTask<R> redisTask) {
        return query(null, redisTask);
    }

    public <R, ID> R queryById(ID id, RedisTask<R> redisTask) {
        return query(idKey + id, redisTask, 1, TimeUnit.HOURS);
    }

    public <R, ID> R queryById(ID id, RedisTask<R> redisTask, long timeout, TimeUnit unit) {
        return query(idKey + id, redisTask, timeout, unit);
    }

    public <R> R query(String keyAppend, RedisTask<R> redisTask) {
        return query(keyAppend, redisTask, 1, TimeUnit.HOURS);
    }

    public <R> R query(String keyAppend, RedisTask<R> redisTask, long timeout, TimeUnit unit) {
        if (disabled) {
            return redisTask.execute();
        }
        if (StringUtil.isNullOrEmpty(keyAppend) || timeout == 0) {
            return redisTask.execute();
        } else if (timeout < 0) {
            R result = redisTask.execute();
            setRedisCache(keyAppend, result, -timeout, unit);
            return result;
        } else {
            R result = getRedisCache(keyAppend);
            if (result == null) {
                result = redisTask.execute();
                setRedisCache(keyAppend, result, timeout, unit);
            }
            return result;
        }
    }

    public <R> R save(RedisTask<R> redisTask) {
        return execute(null, redisTask);
    }

    public void save(RedisVoidTask redisVoidTask) {
        execute(null, redisVoidTask);
    }

    public <R, ID> R updateById(ID id, RedisTask<R> redisTask) {
        return execute(idKey + id, redisTask);
    }

    public <ID> void updateById(ID id, RedisVoidTask redisVoidTask) {
        execute(idKey + id, redisVoidTask);
    }


    public <ID> void delById(ID id, RedisVoidTask redisVoidTask) {
        del(idKey + id, redisVoidTask);
    }

    public void del(RedisVoidTask redisVoidTask) {
        del(null, redisVoidTask);
    }

    public void del(String keyAppend, RedisVoidTask redisVoidTask) {
        execute(keyAppend, redisVoidTask);
    }

    public <R, ID> R delById(ID id, RedisTask<R> redisTask) {
        return del(idKey + id, redisTask);
    }

    public <R> R del(RedisTask<R> redisTask) {
        return del(null, redisTask);
    }

    public <R> R del(String keyAppend, RedisTask<R> redisTask) {
        return execute(keyAppend, redisTask);
    }

    public <R> R execute(String keyAppend, RedisTask<R> redisTask) {
        if (disabled) {
            return redisTask.execute();
        }
        R result = redisTask.execute();
        clearCacheKey(keyAppend);
        return result;
    }

    public void execute(String keyAppend, RedisVoidTask redisVoidTask) {
        if (disabled) {
            redisVoidTask.execute();
            return;
        }
        redisVoidTask.execute();
        clearCacheKey(keyAppend);
    }

    /**
     * 删除对应的ID缓存
     */
    public <ID> void deleteIdCacheKey(ID id) {
        deleteCacheKey(idKey + id);
    }

    /**
     * 删除单条redis缓存
     */
    public void deleteCacheKey(String key) {
        if (disabled) {
            return;
        }
        redisTemplate.delete(getRealKeyPrefix(key));
    }

    /**
     * 清除列表缓存
     */
    public void clearCacheList() {
        clearCache("list_*");
    }

    /**
     * 删除单条redis缓存和列表缓存
     */
    public void clearCacheKey(String key) {
        if (StringUtil.isNotNullOrEmpty(key)) {
            deleteCacheKey(key);
        }
        clearCacheList();
    }

    /**
     * 清除本类全部缓存
     */
    public void clearCache() {
        clearCache("*");
    }

    /**
     * 按key值清除缓存
     *
     * @param pattern key,支持通配符<br/>
     *                <B>*</B> ：匹配任意个字符<br/>
     *                <B>?</B> ：匹配一个任意字符<br/>
     *                <B>h[ae]llo</B> 匹配 hello 和 hallo ，但不匹配 hillo 。
     */
    public void clearCache(String pattern) {
        if (disabled) {
            return;
        }
        Set<String> keys = redisTemplate.keys(getRealKeyPrefix(pattern));
        if (keys != null)
            redisTemplate.delete(keys);
    }

    /**
     * 设置缓存，默认1小时
     */
    public <V> void setRedisCache(String key, V value) {
        setRedisCache(key, value, 1, TimeUnit.HOURS);
    }

    /**
     * 设置有时效的缓存
     */
    public <V> void setRedisCache(String key, V value, long timeout, TimeUnit unit) {
        if (disabled) {
            return;
        }
        if (value != null) {
            redisTemplate.opsForValue().set(getRealKeyPrefix(key), value, timeout, unit);
        }
    }

    public <V> V getRedisCache(String key) {
        if (disabled) {
            return null;
        }
        try {
            String realKey = getRealKeyPrefix(key);
            if (redisTemplate.hasKey(realKey)) {
                return (V) redisTemplate.opsForValue().get(realKey);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private String getRealKeyPrefix(String key) {
        return "cache_" + keyPrefix + "_" + key;
    }
}
