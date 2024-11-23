package org.example.user.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("条件查询d参数")
public class UserQuery extends PageQuery {
    @ApiModelProperty
    private String name;
    @ApiModelProperty("状态")
    private Integer status;
    @ApiModelProperty("最小金都发生额")
    private Integer minBalance;
    @ApiModelProperty("最大余额")
    private Integer maxBalance;
}



