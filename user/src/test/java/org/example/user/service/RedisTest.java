package org.example.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class RedisTest {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void test() {
        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<>();
        tuples.add(new DefaultTypedTuple<>("zhao", 100.10)); // 合法，因为 DefaultTypedTuple 是 TypedTuple 的实现
        tuples.add(new DefaultTypedTuple<>("qian", 99.10)); // 合法，因为 DefaultTypedTuple 是 TypedTuple 的实现
        tuples.add(new DefaultTypedTuple<>("sun", 102.10)); // 合法，因为 DefaultTypedTuple 是 TypedTuple 的实现


        redisTemplate.opsForZSet().add("榜单", tuples);

        redisTemplate.opsForHash().put("user01", "name", "zhang");

        redisTemplate.opsForList().leftPop("34311");

        redisTemplate.opsForSet().add("323234", "1123", 34234);

        redisTemplate.opsForValue().increment("123", 22);
        redisTemplate.opsForValue().set("111", "22");
    }
}
