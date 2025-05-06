package org.example.rpc.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserDto implements Cloneable{
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

    @Override
    public UserDto clone() {
        try {
            UserDto clone = (UserDto) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
