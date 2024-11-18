package org.example.user.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.example.user.entity.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void queryUsersAndAddress() {
        List<User> list = userService.list();
        list.forEach(System.out::println);
    }

    @Test
    void queryUsersAndAddressByIds() {
    }

    /**
     * 分页查询
     */
    @Test
    public void pageList() {
        int pageNum = 1;
        int pageSize = 3;
        String paramName = "王";
        //分页条件
        Page<User> page = Page.of(pageNum, pageSize);
        //排序条件
        page.addOrder(OrderItem.descs("balance", "id"));

//        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.like(StrUtil.isNotBlank(paramName), User::getName, paramName);
////
        LambdaQueryChainWrapper<User> like = Db.lambdaQuery(User.class)
                .like(StrUtil.isNotBlank(paramName), User::getName, paramName);
//
//        //分页查询
        userService.page(page, like.getWrapper());

        System.out.println("pages= " + page.getPages());
        System.out.println("total= " + page.getTotal());
        page.getRecords().forEach(System.out::println);
    }
}