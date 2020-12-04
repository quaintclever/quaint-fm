package com.github.quaintclever.cache.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * desc: redis 缓存服务
 * </p>
 *
 * @author quaint
 * @since 04 December 2020
 */
@Component
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * redis 执行成功标识
     */
    private static final Long SUCCESS = 1L;

    /**
     * 缓存前缀
     */
    private static final String PREFIX = "quaint_";


    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return bool
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(wrapperPrefix(key), time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */

    public Long getExpire(String key) {
        return redisTemplate.getExpire(wrapperPrefix(key), TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(wrapperPrefix(key));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值
     */
    public void delete(String key) {
        redisTemplate.delete(wrapperPrefix(key));
    }

    /**
     * 根据pattern删除缓存
     * 示例hello*
     *
     * @param pattern pattern
     */
    public void delPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(wrapperPrefix(pattern));
        if (!CollectionUtils.isEmpty(keys)) {
            keys.forEach(key -> redisTemplate.delete(key));
        }
    }

    /**
     * 根据pattern删除缓存(无prefix)
     *
     * @param pattern pattern
     */
    public void delPatternNoPrefix(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (!CollectionUtils.isEmpty(keys)) {
            keys.forEach(key -> redisTemplate.delete(key));
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(wrapperPrefix(key));
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(wrapperPrefix(key), value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(wrapperPrefix(key), value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 递增
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public Long increment(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(wrapperPrefix(key), delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return 147
     */
    public Long decreasing(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(wrapperPrefix(key), -delta);
    }

    /**
     * 查询符合规则的所有值
     *
     * @param pattern pattern
     * @return set
     */
    public Set<String> keys(String pattern) {
        if (pattern != null && !StringUtils.isEmpty(pattern.trim())) {
            return redisTemplate.keys(wrapperPrefix(pattern));
        } else {
            throw new RuntimeException("匹配规则不能为空");
        }
    }

    /**
     * 获取分布式锁
     *
     * @param lockKey     锁
     * @param requestId   请求标识
     * @param expireTime  单位秒
     * @param waitTimeout 单位毫秒
     * @return 是否获取成功
     */
    public boolean tryLock(String lockKey, String requestId, int expireTime, long waitTimeout) {

        lockKey = wrapperPrefix(lockKey);
        // 当前时间
        long nanoTime = System.nanoTime();
        try {
            String script = "if redis.call('setNx',KEYS[1],ARGV[1]) then if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end end";
            log.info("开始获取分布式锁-key[{}]", lockKey);
            int count = 0;
            do {
                RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);

                log.debug("尝试获取分布式锁-key[{}]requestId[{}]count[{}]", lockKey, requestId, count);
                Object result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey), requestId, expireTime);

                if (SUCCESS.equals(result)) {
                    log.debug("尝试获取分布式锁-key[{}]成功", lockKey);
                    return true;
                }
                // 休眠500毫秒
                Thread.sleep(500L);
                count++;
            } while ((System.nanoTime() - nanoTime) < TimeUnit.MILLISECONDS.toNanos(waitTimeout));

        } catch (Exception e) {
            log.error("尝试获取分布式锁-key[" + lockKey + "]异常", e);
        }

        log.warn("获取分布式锁-key[{}]失败", lockKey);

        return false;
    }

    /**
     * 释放锁
     *
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean releaseLock(String lockKey, String requestId) {

        lockKey = wrapperPrefix(lockKey);
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

        RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);

        Object result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey), requestId);
        if (SUCCESS.equals(result)) {
            log.info("释放分布式锁-key[{}]成功", lockKey);
            return true;
        }

        log.warn("释放分布式锁-key[{}]失败", lockKey);
        return false;

    }

    /**
     * 包装前缀
     * @param key key
     * @return prefix + key
     */
    private String wrapperPrefix(String key) {
        return PREFIX + key;
    }
}
