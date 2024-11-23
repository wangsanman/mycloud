package org.example.user;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.user.config.JwtUtil;
import org.example.user.entity.po.User;
import org.example.user.mapper.UserMapper;
import org.example.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@SpringBootTest
@Slf4j
//@RequiredArgsConstructor
class UserApplicationTests {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 测试 mybatis 非空不修改
     */
    @Test
    void contextLoads() {
        User user = new User();
        user.setId(1L);
        user.setAge(1);
        user.setName("");
        userService.updateById(user);
        CollUtil.join(new ArrayList<>(),",");
        Map map = new HashMap();
        map.put("ids","1,2,3,4,5,6,7,8,9,10");

        ResponseEntity<List<User>> exchange = restTemplate.exchange(
                "http://localhost:8081/userService/users/ids/{ids}",

                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>() {
                },
                map
        );

        if (exchange.getStatusCode().is2xxSuccessful()) {
            System.out.println(exchange.getStatusCodeValue());
        }

        exchange.getBody().forEach(System.out::println);

    }

    @Test
    void tokenTest() {
        String s = jwtUtil.generateToken("abcd312345");
        log.info("token: {}", s);
        jwtUtil.verifyToken(s);

        String userId = jwtUtil.getUserId(s);
        log.info("userId: {}", userId);


    }


    @Test
    void testQueryWrapper() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("id", "name", "password");
        wrapper.ge("age", 18);
        List<User> users = userMapper.selectList(wrapper);

        System.out.println(users);
        try {
            Integer a = new Integer(1);
            Integer b = new Integer(0);
            Integer c = a / b;
        } catch (Exception e) {
            log.error("异常了", e);
        }

    }

    @Test
    void testUpdateWrapper() {
        User user = new User();
        user.setAge(20);
        //将id1,2,3的金额都减200
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("balance = balance - 200");
        updateWrapper.in("id", Arrays.asList(1L, 2L, 3L));
        userMapper.update(user, updateWrapper);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

    }

    @Test
    void testLambdaQueryWrapper() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(User::getId, User::getName, User::getPassword);
        userMapper.selectList(queryWrapper);
    }

    @Test
    void testCustomSqlUpdate() {
        //将id1,2,3的金额都减200
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<User> lambda = queryWrapper.lambda();
        queryWrapper.in("id", Arrays.asList(1L, 2L, 3L));
        userMapper.updateByBalanceByIds(200, queryWrapper);
    }

    //批处理
    @Test
    void testBatchInsert() {
        List<User> users = new ArrayList<>(1000);
        long a = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            User user = new User();
            user.setAge(20);
            user.setName("user" + i);
            user.setBalance(1000);
            user.setPassword(123456);
            users.add(user);

            if (i % 1000 == 0) {
                userService.saveBatch(users);
                users.clear();
            }
        }
        System.out.println("耗时" + (System.currentTimeMillis() - a));
    }
}
