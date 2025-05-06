package org.example.user;

import cn.hutool.core.lang.func.VoidFunc0;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import org.example.common.RedisApi;
import org.example.common.RedisConstants;
import org.example.common.RedisIdWorker;
import org.example.user.entity.po.User;
import org.example.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisStringTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testString() {
        String str = JSONUtil.toJsonStr(new User(1L, "王一满啊", 28));
        stringRedisTemplate.opsForValue().set("您好啊", str);
        Object hello = stringRedisTemplate.opsForValue().get("您好啊");
        System.out.println("hello = " + hello);
    }

    @Test
    public void testHash() {
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.put("user::400","id","1");
        hashOperations.put("user::400","name","张三");
        hashOperations.put("user::400","age","28");
        String name = hashOperations.get("user::400", "name");
        Map<String, String> entries = hashOperations.entries("user::400");

        System.out.println("hashOperations.get(\"user::400\",\"name\") = " + hashOperations.get("user::400", "name"));
        System.out.println("hashOperations.entries(\"user::400\") = " + hashOperations.entries("user::400"));
    }

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RedisApi redisApi;
    @Autowired
    private RedisIdWorker redisIdWorker;

    private ExecutorService executorService = Executors.newFixedThreadPool(500);

    @Test
    public void testList() throws InterruptedException {
        User byId = userService.getById(6);
        redisApi.saveExpireObj(RedisConstants.CACHE_USER_PREFIX+6,byId,10L, TimeUnit.SECONDS);
    }

    @Test
    public void testSet() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(500);

        Runnable task = () -> {
            for (int i = 0; i < 100; i++) {
                long id = redisIdWorker.nextId("order");
                System.out.println("id = " + id);
            }
            countDownLatch.countDown();
        };

        long l = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            executorService.submit(task);

        }
        countDownLatch.await();
        long l1 = System.currentTimeMillis();
        System.out.println("l1-l = " + (l1-l));
    }
}
