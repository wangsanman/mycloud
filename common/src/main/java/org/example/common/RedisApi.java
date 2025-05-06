package org.example.common;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.example.common.RedisConstants.DISTRIBUTE_LOCK_TTL;

@Component
@RequiredArgsConstructor
public class RedisApi {
    private final StringRedisTemplate stringRedisTemplate;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final RedisTemplate redisTemplate;

    private final static DefaultRedisScript<Long> redisScript;

    static {
        redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(new ClassPathResource("unlock.lua"));
        redisScript.setResultType(Long.class);
    }

    /**
     * 保存对象
     *
     * @param key
     * @param value
     * @param timeout 单位分钟
     * @return
     */
    public void save(String key, Object value, long timeout) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), timeout, TimeUnit.MINUTES);
    }

    /**
     * 获取对象
     */
    public <T> T get(String key, Class<T> clazz) {
        String obj = stringRedisTemplate.opsForValue().get(key);
        if (obj == null) {
            return null;
        }
        return JSONUtil.toBean(obj, clazz);
    }

    /**
     * 保存对象逻辑过期
     */
    public void saveExpireObj(String key, Object object, long timeout, TimeUnit timeUnit) {
        RedisExpireTime<Object> redisExpireObj = new RedisExpireTime<>();
        redisExpireObj.setExpireTime(LocalDateTime.now().plusSeconds(timeUnit.toSeconds(timeout)));
        redisExpireObj.setData(object);

        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisExpireObj));
    }

    /**
     * 获取逻辑过期对象
     */
    public <T, ID> T getExpireObj(String prefix, ID id, long timeout, TimeUnit timeUnit, Class<T> clazz, Function<ID, T> function) {
        String obj = stringRedisTemplate.opsForValue().get(prefix + id);
        //查到直接返回(包括空串“”)直接返回
        if ("".equals(obj)) {
            return null;
        }
        T data = null;
        if (obj != null) {
            //转换为对象
            RedisExpireTime<JSONObject> bean = JSONUtil.toBean(obj, RedisExpireTime.class);
            data = JSONUtil.toBean(bean.getData(), clazz);
            //判断是否过期
            if (LocalDateTime.now().isBefore(bean.getExpireTime())) {
                //没过期直接返回
                return data;
            }
        }

        if (!lock(RedisConstants.DISTRIBUTE_LOCK_PREFIXES + id)) {
            //加锁失败,返回过期数据
            return data;
        }


        //加锁成功
        //过期了, 重建缓存
        executorService.submit(() -> {
            try {
                T apply = function.apply(id);
                saveExpireObj(prefix + id, apply, timeout, timeUnit);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                //确保释放锁
                unlock(RedisConstants.DISTRIBUTE_LOCK_PREFIXES + id);
            }
        });

        //查到了,保存到redis


        return data;
    }

    private static final String ID_PREFIX = UUID.randomUUID().toString(true)+"-";

    /**
     * 加锁
     *
     * @param lockKey
     * @return
     */
    public boolean lock(String lockKey) {
        Boolean b = stringRedisTemplate.opsForValue().setIfAbsent(RedisConstants.DISTRIBUTE_LOCK_PREFIXES+lockKey,ID_PREFIX+ Thread.currentThread().getId(), DISTRIBUTE_LOCK_TTL, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(b);
    }

    /**
     * 解锁,利用lua脚本
     *
     * @param lockKey
     */
    public void unlock(String lockKey) {
        stringRedisTemplate.execute(redisScript,
                Collections.singletonList(RedisConstants.DISTRIBUTE_LOCK_PREFIXES + lockKey),
                ID_PREFIX+Thread.currentThread().getId());
    }
//    /**
//     * 解锁
//     *
//     * @param lockKey
//     * @return
//     */
//    public boolean unlock(String lockKey) {
//        String string = stringRedisTemplate.opsForValue().get(RedisConstants.DISTRIBUTE_LOCK_PREFIXES + lockKey);
//        if ((ID_PREFIX+Thread.currentThread().getId()).equals(string)) {
//            return Boolean.TRUE.equals(stringRedisTemplate.delete(RedisConstants.DISTRIBUTE_LOCK_PREFIXES + lockKey));
//        }
//        return false;
//    }

}
