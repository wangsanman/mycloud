package org.example.user.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.example.user.entity.enums.UserEnum;
import org.example.user.entity.po.Address;
import org.example.user.entity.dto.UserInfo;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserVo {
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("状态")
    private UserEnum status;

    @ApiModelProperty("详细信息")
    private UserInfo info;

    @ApiModelProperty("账户余额")
    private Integer balance;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("用户地址")
    private List<Address> address;
}
