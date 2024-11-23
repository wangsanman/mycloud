package org.example.order.service.impl;

import org.example.order.entity.Order;
import org.example.order.mapper.OrderMapper;
import org.example.order.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表，存储用户订单的核心信息 服务实现类
 * </p>
 *
 * @author man
 * @since 2024-11-22
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

}
