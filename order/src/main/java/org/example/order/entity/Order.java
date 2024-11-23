package org.example.order.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单表，存储用户订单的核心信息
 * </p>
 *
 * @author man
 * @since 2024-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cloud_order")
@ApiModel(value="Order对象", description="订单表，存储用户订单的核心信息")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单主键 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户 ID，关联用户表")
    private Long userId;

    @ApiModelProperty(value = "订单总金额，包含优惠后的金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "订单状态：0=待支付, 1=已支付, 2=已发货, 3=已完成, 4=已取消")
    private Integer status;

    @ApiModelProperty(value = "订单创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "订单更新时间")
    private LocalDateTime updatedAt;


}
