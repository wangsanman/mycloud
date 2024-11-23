package org.example.order.controller;


import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.example.order.entity.Order;
import org.example.order.entity.vo.OrderUserVo;
import org.example.order.service.IOrderService;
import org.example.rpc.client.UserApi;
import org.example.rpc.dto.UserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单表，存储用户订单的核心信息 前端控制器
 * </p>
 *
 * @author man
 * @since 2024-11-22
 */
@RestController
@RequestMapping("/orders")
@Api(tags = {"订单相关接口"})
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;
    private final UserApi userApi;

    @GetMapping("/orderAndUser/{id}")
    @ApiOperation("查询订单及用户")
    public OrderUserVo getOrderAndUser(@ApiParam("订单id") @PathVariable Integer id){
        Order order = orderService.getById(id);
        UserDto userDto = userApi.get(order.getUserId());

        OrderUserVo orderUserVo = BeanUtil.copyProperties(order, OrderUserVo.class);
        BeanUtil.copyProperties(userDto,orderUserVo,"id","status");
        orderUserVo.setUserStatus(userDto.getStatus());

        return orderUserVo;
    }



}
