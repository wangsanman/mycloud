package org.example.common;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class RedisIdWorker {
    /**
     * 开始时间戳
     */
    private static final long BEGIN_TIMESTAMP = 1577836800L;
    private final StringRedisTemplate stringRedisTemplate;


//    public static void main(String[] args) {
//        LocalDateTime localDateTime = LocalDateTime.of(2020, 1, 1, 0, 0);
//        long epochSecond = localDateTime.toEpochSecond(ZoneOffset.UTC);
//        System.out.println("epochSecond = " + epochSecond);
//
//    }

    public long nextId(String keyPrefix) {
        LocalDateTime now = LocalDateTime.now();
        //当前时间戳
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;

        String format = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));

        //自增并返回
        long increment = stringRedisTemplate.opsForValue().increment("incr:" + keyPrefix + ":" + format);

        //拼接时间戳和自增数
        long l = timestamp << 32 | increment;

        return l;
    }
}
