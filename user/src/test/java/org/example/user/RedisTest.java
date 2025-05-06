package org.example.user;

import org.example.user.entity.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testString() {
        redisTemplate.opsForValue().set("您好", new User(1L,"王一满",28));
        Object hello = redisTemplate.opsForValue().get("您好");
        System.out.println("hello = " + hello);
    }
}
