package org.example.user.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.example.user.entity.po.UserInfo;

@Data
public class UserDto {
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("详细信息")
    private UserInfo info;

    @ApiModelProperty("账户余额")
    private Integer balance;

    @ApiModelProperty("密码")
    private Integer password;

}
