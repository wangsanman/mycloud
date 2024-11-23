package org.example.order.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.example.rpc.dto.UserDto;
import org.example.rpc.dto.UserInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderUserVo {

    @ApiModelProperty(value = "订单主键 ID")
    private Long id;

    @ApiModelProperty(value = "用户 ID，关联用户表")
    private Long userId;

    @ApiModelProperty(value = "订单总金额，包含优惠后的金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "订单状态：0=待支付, 1=已支付, 2=已发货, 3=已完成, 4=已取消")
    private Integer status;

    @ApiModelProperty(value = "订单创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("订单状态")
    private Integer userStatus;

    @ApiModelProperty("详细信息")
    private UserInfo info;

    @ApiModelProperty("账户余额")
    private Integer balance;

    @ApiModelProperty("密码")
    private Integer password;
}
